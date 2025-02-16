package com.gymmanager.controller;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gymmanager.dto.UserResponse;
import com.gymmanager.dto.UserUpdateRequest;
import com.gymmanager.model.User;
import com.gymmanager.security.services.imp.UserServiceImpl;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImp;

    @PatchMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<UserResponse> updateUserProfile(
    @PathVariable Long userId,
    @Valid @RequestBody UserUpdateRequest request
    ) {
        User updatedUser = userServiceImp.updateUserProfile(userId, request);
        return ResponseEntity.ok(toUserResponse(updatedUser));
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

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<UserResponse> getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userServiceImp.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(toUserResponse(user));
    }

    
}
