package com.gymmanager.dto;

import java.time.LocalDate;

import com.gymmanager.model.Subscription;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

public class SubscriptionDto {
    
    @Data
    public static class Create {
        @NotNull
        private Long customerId;
        
        @NotNull
        private Long packId;
    }

    @Data
    @Builder
    public static class Response {
        private Long id;
        private LocalDate startDate;
        private boolean active;
        private CustomerDto.Response customer;
        private PackDto.Response pack;

        // Méthode fromEntity ajoutée
        public static Response fromEntity(Subscription subscription) {
            return Response.builder()
                .id(subscription.getId())
                .startDate(subscription.getStartDate())
                .active(subscription.isActive())
                .pack(PackDto.Response.fromEntity(subscription.getPack()))
                .customer(CustomerDto.Response.fromEntity(subscription.getCustomer()))
                .build();
        }
    }
}
