package com.gymmanager.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreateRequest {
    @NotBlank
    private String username;
    
    @NotBlank
    @Size(min = 6)
    private String password;
    
    @NotNull
    private String role;

    
    
}
