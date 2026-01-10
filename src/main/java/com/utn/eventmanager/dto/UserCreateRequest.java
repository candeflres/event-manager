package com.utn.eventmanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// esta clase se utiliza para el REGISTRO únicamente
// no tiene role pq lo asigna el sistema, pero tiene password

public class UserCreateRequest {
    @NotBlank
    @Size(max = 20)
    private String firstName;

    @NotBlank
    @Size(max = 20)
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(max = 20)
    private String phone;

    @NotBlank
    @Size(min = 8)
    private String password;
}
