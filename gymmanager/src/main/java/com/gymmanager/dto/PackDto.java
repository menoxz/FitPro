package com.gymmanager.dto;

import com.gymmanager.model.Pack;
import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

public record PackDto(
    @NotBlank(message = "Le nom de l'offre est obligatoire") 
    String name,
    
    @Positive(message = "La durée doit être un nombre positif") 
    Integer durationInMonths,
    
    @Positive(message = "Le prix mensuel doit être positif") 
    BigDecimal monthlyPrice
) {
    public static PackDto fromEntity(Pack pack) {
        return new PackDto(
            pack.getName(),
            pack.getDurationInMonths(),
            pack.getMonthlyPrice()
        );
    }

    // Pour les réponses API
     @Builder
    public record Response(
        Long id,
        String name,
        Integer durationInMonths,
        BigDecimal monthlyPrice
    ) {
        public static Response fromEntity(Pack pack) {
            return new Response(
                pack.getId(),
                pack.getName(),
                pack.getDurationInMonths(),
                pack.getMonthlyPrice()
            );
        }
    }
}
