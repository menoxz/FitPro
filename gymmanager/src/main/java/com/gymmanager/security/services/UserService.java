package com.gymmanager.security.services;

import javax.management.relation.RoleNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gymmanager.model.ERole;
import com.gymmanager.model.User;
import com.gymmanager.payload.request.SignupRequest;

public interface UserService {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void createUser(SignupRequest signUpRequest);
    User updateUserRole(Long userId, ERole newRole) throws RoleNotFoundException;
    // Dans UserService.java
    void deleteUser(Long userId);
    Page<User> getAllUsers(Pageable pageable);
    User manageUserSubscription(Long userId, Long packId);

    // get the user role
    String getUserRole(Long userId);

}
