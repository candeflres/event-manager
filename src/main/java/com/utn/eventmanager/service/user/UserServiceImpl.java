package com.utn.eventmanager.service.user;

import com.utn.eventmanager.dto.user.EmployeeCreateRequest;
import com.utn.eventmanager.dto.user.UserCreateRequest;
import com.utn.eventmanager.dto.user.UserResponse;
import com.utn.eventmanager.model.Event;
import com.utn.eventmanager.model.User;
import com.utn.eventmanager.model.enums.EventStatus;
import com.utn.eventmanager.model.enums.UserRole;
import com.utn.eventmanager.repository.EventRepository;
import com.utn.eventmanager.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EventRepository eventRepository;


    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            EventRepository eventRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventRepository = eventRepository;
    }

    @Override
    public UserResponse createUser(UserCreateRequest request) {

        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new IllegalStateException("El email ya está registrado");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        // rol para crear cliente
        user.setRole(UserRole.CLIENT);

        user.setActive(true);
        user.setCreated(LocalDateTime.now());

        // password temporal PARA PRUEBAS EN POSTMAN UNICAMENTE !
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return mapToResponse(userRepository.save(user));
    }


    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public UserResponse getUserById(Long userId) {
        return mapToResponse(findUser(userId));
    }

    // privados

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public UserResponse getMyProfile(Authentication authentication) {
        User user = getUserFromAuth(authentication);
        return mapToResponse(user);
    }

    @Override
    @Transactional
    public void deactivateMyAccount(Authentication authentication) {
        User user = getUserFromAuth(authentication);

        deactivateUser(user.getId());
    }
    @Override
    @Transactional
    public void deactivateUser(Long userId) {
        System.out.println(">>> ENTRO A deactivateUser, userId = " + userId);

        User user = findUser(userId);

        if (user.getRole() == UserRole.CLIENT) {
            System.out.println(">>> USUARIO ES CLIENT");


            List<Event> eventsToCancel =
                    eventRepository
                            .findByUserIdAndStatusNot(
                                    user.getId(),
                                    EventStatus.COMPLETED,
                                    Pageable.unpaged()
                            )
                            .getContent();

            for (Event event : eventsToCancel) {
                System.out.println(">>> CANCELANDO EVENTO " + event.getId() +
                        " status actual = " + event.getStatus());
                event.setStatus(EventStatus.CANCELLED);
            }

            eventRepository.saveAll(eventsToCancel);
        }

        user.setActive(false);
        userRepository.save(user);
    }

    private UserResponse mapToResponse(User user) {
        UserResponse res = new UserResponse();
        res.setId(user.getId());
        res.setFirstName(user.getFirstName());
        res.setLastName(user.getLastName());
        res.setEmail(user.getEmail());
        res.setPhone(user.getPhone());
        res.setRole(user.getRole());
        res.setActive(user.getActive());

        if (user.getCreated() != null) {
            res.setCreated(user.getCreated().toLocalDate());
        }

        return res;
    }
    @Override
    public User getUserFromAuth(Authentication authentication) {
        if (authentication == null) {
            throw new RuntimeException("Authentication is null");
        }

        String email = authentication.getName();

        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado para email: " + email)
                );
    }
    // crear empleado como admin !
    @Override
    public UserResponse createEmployee(EmployeeCreateRequest request) {

        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new IllegalStateException("El email ya está registrado");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole(UserRole.EMPLOYEE);
        user.setActive(true);
        user.setCreated(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return mapToResponse(userRepository.save(user));
    }
    @Override
    public List<UserResponse> getAllUsersIncludingInactive() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
}