package com.gymmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@Schema(description = "Réponse utilisateur avec informations détaillées")
public class UserResponse {
    
    @Schema(description = "ID unique de l'utilisateur", example = "123")
    private Long id;
    
    @Schema(description = "Nom d'utilisateur unique", example = "jeanl")
    private String username;
    
    @Schema(description = "Email de l'utilisateur", example = "jean@gmail.com")
    private String email;

    @Schema(description = " Contact l'utilisateur", example = "97126162")
    private String phoneNumber;
    
    @Schema(description = "Rôles attribués à l'utilisateur", 
            example = "[\"ROLE_ADMIN\", \"ROLE_STAFF\"]")
    private Set<String> roles;
    
    

    public UserResponse(Long id, String username, String email, String phoneNumber, Set<String> roles ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.roles = roles;
       
    }

   
    
}
