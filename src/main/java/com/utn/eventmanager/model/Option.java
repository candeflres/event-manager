package com.utn.eventmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
@Table(name = "options")
public class Option {

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
    @PositiveOrZero
    private BigDecimal price;

    @NotNull
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "element_id", nullable = false)
    private Element element;
}