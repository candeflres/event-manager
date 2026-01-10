package com.utn.eventmanager.dto;

import com.utn.eventmanager.model.enums.UserRole;

import java.time.LocalDate;

// esta clase se usa para RESPUESTAS
// ya que no contiene password ni datos sensibles
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserRole role;
    private Boolean active;
    private LocalDate createdAt;
}
