package com.utn.eventmanager.model;

import com.utn.eventmanager.model.enums.EventStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "events")
public class Event {

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
    @Column(nullable = false)
    private LocalDate eventDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private EventStatus status;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private BigDecimal estimatedBudget;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
