package com.utn.eventmanager.repository;

import com.utn.eventmanager.model.User;
import com.utn.eventmanager.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Buscar usuario por email (login, recuperación, validaciones)
    Optional<User> findByEmail(String email);

    // Validar duplicados de email
    boolean existsByEmailIgnoreCase(String email);

    // Listar usuarios por rol (empleados, admins, clientes)
    List<User> findByRole(UserRole role);

    // Listar solo usuarios activos
    List<User> findByActiveTrue();

    // Buscar usuario activo por email (login futuro)
    Optional<User> findByEmailAndActiveTrue(String email);
}
