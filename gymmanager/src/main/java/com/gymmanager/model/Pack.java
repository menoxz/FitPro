package com.gymmanager.model;
import java.math.BigDecimal;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Table(name = "packs")
@Data
public class Pack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom de l'offre est obligatoire")
    private String name;

    @Positive(message = "La durée doit être un nombre positif")
    private int durationInMonths;

    @Positive(message = "Le prix mensuel doit être positif")
    private BigDecimal monthlyPrice;
}
