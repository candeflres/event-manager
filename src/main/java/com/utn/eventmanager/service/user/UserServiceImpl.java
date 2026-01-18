package com.utn.eventmanager.service.user;

import com.utn.eventmanager.dto.user.EmployeeCreateRequest;
import com.utn.eventmanager.dto.user.UserCreateRequest;
import com.utn.eventmanager.dto.user.UserResponse;
import com.utn.eventmanager.model.User;
import com.utn.eventmanager.model.enums.UserRole;
import com.utn.eventmanager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    @Override
    public void deactivateUser(Long userId) {
        User user = findUser(userId);
        user.setActive(false);
        userRepository.save(user);
    }

    // privados

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
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
        return res;
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