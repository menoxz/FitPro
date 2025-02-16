package com.gymmanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
public class UserUpdateRequest {
    @Email
    private String email;
    
    @Size(min = 6, max = 40)
    private String password;
    
    private String phoneNumber;

}
