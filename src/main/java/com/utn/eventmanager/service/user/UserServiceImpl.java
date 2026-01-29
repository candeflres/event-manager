package com.utn.eventmanager.service.user;

import com.utn.eventmanager.dto.user.*;
import com.utn.eventmanager.model.Event;
import com.utn.eventmanager.model.User;
import com.utn.eventmanager.model.enums.AuditAction;
import com.utn.eventmanager.model.enums.AuditEntity;
import com.utn.eventmanager.model.enums.EventStatus;
import com.utn.eventmanager.model.enums.UserRole;
import com.utn.eventmanager.repository.EventRepository;
import com.utn.eventmanager.repository.UserRepository;
import com.utn.eventmanager.service.audit.AuditLogService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.security.access.AccessDeniedException;import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EventRepository eventRepository;
    private final AuditLogService auditLogService;


    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            EventRepository eventRepository,
            AuditLogService auditLogService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventRepository = eventRepository;
        this.auditLogService = auditLogService;
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
        user.setRole(UserRole.CLIENT);
        user.setActive(true);
        user.setCreated(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user = userRepository.save(user);

        auditLogService.log(
                AuditAction.CREATE,
                AuditEntity.USER,
                "Creó cuenta de cliente: " + user.getEmail(),
                user
        );

        return mapToResponse(user);
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
    public UserResponse updateMyProfile(Authentication authentication, UserUpdateRequest request) {

        User user = getUserFromAuth(authentication);

        if (!user.getEmail().equalsIgnoreCase(request.getEmail())
                && userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new IllegalStateException("El email ya está en uso");
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        auditLogService.log(
                AuditAction.UPDATE,
                AuditEntity.USER,
                "Actualizó su perfil: " + user.getEmail(),
                user
        );

        return mapToResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public void changeMyPassword(Authentication authentication, ChangePasswordRequest request) {

        User user = getUserFromAuth(authentication);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("La contraseña actual es incorrecta");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        auditLogService.log(
                AuditAction.UPDATE,
                AuditEntity.USER,
                "Cambió su contraseña desde el perfil: " + user.getEmail(),
                user
        );
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deactivateMyAccount(Authentication authentication) {
        User user = getUserFromAuth(authentication);

        auditLogService.log(
                AuditAction.DELETE,
                AuditEntity.USER,
                "Dio de baja su cuenta: " + user.getEmail(),
                user
        );
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
    @Override
    @Transactional
    public void deactivateUserByAdmin(
            Authentication authentication,
            Long userId
    ) {

        User admin = userRepository
                .findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));

        if (admin.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Solo un ADMIN puede dar de baja usuarios");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario a desactivar no encontrado"));

        auditLogService.log(
                AuditAction.DELETE,
                AuditEntity.USER,
                "El admin dio de baja la cuenta: " + user.getEmail(),
                admin
        );

        deactivateUser(userId);
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
    public UserResponse createEmployee(
            Authentication authentication,
            EmployeeCreateRequest request
    ) {

        User admin = userRepository
                .findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));

        if (admin.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Solo un ADMIN puede crear empleados");
        }

        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new IllegalStateException("El email ya está registrado");
        }


        User employee = new User();
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setRole(UserRole.EMPLOYEE);
        employee.setActive(true);
        employee.setCreated(LocalDateTime.now());
        employee.setPassword(passwordEncoder.encode(request.getPassword()));

        auditLogService.log(
                AuditAction.CREATE,
                AuditEntity.USER,
                "El admin creó el empleado: " + employee.getEmail(),
                admin
        );
        return mapToResponse(userRepository.save(employee));
    }
    @Override
    public List<UserResponse> getAllUsersIncludingInactive() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<UserResponse> getEmployees() {
        return userRepository.findByRoleAndActiveTrue(UserRole.EMPLOYEE)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public UserResponse updateUserByAdmin(
            Authentication authentication,
            Long userId,
            UserUpdateRequest request
    ) {

        User admin = userRepository
                .findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));

        if (admin.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Solo ADMIN puede editar usuarios");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!user.getEmail().equalsIgnoreCase(request.getEmail())
                && userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new IllegalStateException("El email ya está registrado");
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        auditLogService.log(
                AuditAction.UPDATE,
                AuditEntity.USER,
                "El admin actualizó el user: " + user.getEmail(),
                admin
        );

        return mapToResponse(userRepository.save(user));
    }
}