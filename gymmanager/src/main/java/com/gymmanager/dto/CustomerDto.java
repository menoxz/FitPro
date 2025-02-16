package com.gymmanager.dto;

import com.gymmanager.model.Customer;
import com.gymmanager.model.Subscription;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

public class CustomerDto {

    @Data
    public static class Request {
        @NotBlank
        private String firstName;
        
        @NotBlank
        private String lastName;

        @NotBlank
        @Pattern(regexp = "^\\d{8}$", message = "Format national requis")
        private String phone;
    }

    @Builder
    @Data
    public static class Response {
        private Long id;
        private String fullName;
        private String phone;
        private String registrationDate;
        private SubscriptionDto.Response activeSubscription; 
        private int subscriptionCount; 


        public static Response fromEntity(Customer customer) {

            return Response.builder()
                .id(customer.getId())
                .fullName(customer.getFirstName() + " " + customer.getLastName())
                .phone(customer.getPhone())
                .registrationDate(customer.getRegistrationDate().toString())
                .subscriptionCount(
                    customer.getSubscriptions() != null ? 
                    customer.getSubscriptions().size() : 
                    0
                )                .build();
        }
        
    }

    
}
