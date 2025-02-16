// Fichier : src/main/java/com/gymmanager/controller/AdminController.java
package com.gymmanager.controller;

import com.gymmanager.dto.UserCreateRequest;
import com.gymmanager.dto.UserResponse;
import com.gymmanager.model.ERole;
import com.gymmanager.model.User;
import com.gymmanager.payload.response.ErrorResponse;
import com.gymmanager.security.exception.GlobalExceptionHandler.SelfRoleModificationException;
import com.gymmanager.security.services.UserService;
import com.gymmanager.security.services.imp.UserServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import javax.management.relation.RoleNotFoundException;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@Tag(name = "Administration", description = "Endpoints pour la gestion administrative")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final UserService userService;
    private final UserServiceImpl userServiceImpl;

    /**
     * Crée un nouvel utilisateur avec un rôle spécifique
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer un utilisateur", description = "Création d'un nouvel utilisateur avec attribution de rôle")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "404", description = "Rôle non trouvé")
    })
    public UserResponse createUser(@Valid @RequestBody UserCreateRequest request) {
        User user = userServiceImpl.createUserWithRole(request);
        return toUserResponse(user);
    }

    /**
     * Modifie le rôle d'un utilisateur existant
     * @throws RoleNotFoundException 
     */
    @PatchMapping("/{userId}/role")
    @Operation(summary = "Modifier un rôle", description = "Modification du rôle d'un utilisateur existant")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Rôle modifié avec succès"),
        @ApiResponse(responseCode = "400", description = "ID utilisateur invalide"),
        @ApiResponse(responseCode = "403", description = "Auto-modification interdite")
    })
    public UserResponse updateUserRole(
        @Parameter(description = "ID de l'utilisateur cible") @PathVariable Long userId,
        @Parameter(description = "Nouveau rôle à attribuer") @RequestParam ERole newRole
    ) throws RoleNotFoundException {
        User updatedUser = userServiceImpl.updateUserRole(userId, newRole);
        return toUserResponse(updatedUser);
    }

    /**
     * Supprime un utilisateur (Nouvelle méthode)
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un utilisateur", description = "Suppression définitive d'un compte utilisateur")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Utilisateur supprimé"),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

    /**
     * Liste paginée des utilisateurs (Nouvelle méthode)
     */
    @GetMapping
    @Operation(summary = "Lister les utilisateurs", description = "Récupération paginée de tous les utilisateurs")
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable)
            .map(this::toUserResponse);
    }



    private UserResponse toUserResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet())
        );
    }

    @ExceptionHandler(com.gymmanager.security.exception.GlobalExceptionHandler.SelfRoleModificationException.class)
    public ResponseEntity<ErrorResponse> handleSelfRoleModification(SelfRoleModificationException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(new ErrorResponse(
                "SELF_ROLE_MODIFICATION",
                ex.getMessage()
            ));
    }
}
