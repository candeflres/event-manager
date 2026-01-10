package com.utn.eventmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "elements")
public class Element {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(length = 50, nullable = false)
    private String name;

    @Size(max = 255)
    @Column(length = 255)
    private String description;

    @NotNull
    private Boolean available;
}