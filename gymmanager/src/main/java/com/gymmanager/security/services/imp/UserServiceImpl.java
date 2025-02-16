package com.gymmanager.security.services.imp;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gymmanager.dto.UserCreateRequest;
import com.gymmanager.dto.UserUpdateRequest;
import com.gymmanager.model.ERole;
import com.gymmanager.model.Role;
import com.gymmanager.model.User;
import com.gymmanager.payload.request.MessageResponse;
import com.gymmanager.payload.request.SignupRequest;
import com.gymmanager.repository.RoleRepository;
import com.gymmanager.repository.UserRepository;
import com.gymmanager.security.exception.GlobalExceptionHandler.RoleNotFoundException;
import com.gymmanager.security.exception.GlobalExceptionHandler.SelfDeletionException;
import com.gymmanager.security.exception.GlobalExceptionHandler.SelfRoleModificationException;
import com.gymmanager.security.exception.GlobalExceptionHandler.UserAlreadyExitException;
import com.gymmanager.security.exception.GlobalExceptionHandler.UserNotFoundException;
import com.gymmanager.security.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public void createUser(SignupRequest signUpRequest) {
        User user = new User(
            signUpRequest.getUsername(),
            signUpRequest.getEmail(),
            passwordEncoder.encode(signUpRequest.getPassword()));

        Role userRole = roleRepository.findByName(ERole.ROLE_STAFF)
            .orElseThrow(() -> new RuntimeException("Erreur: Role non trouvé."));
            
        user.getRoles().add(userRole);
        userRepository.save(user);
    }
    
    @Transactional
    public User createUserWithRole(UserCreateRequest request) {
        if (existsByUsername(request.getUsername())) {
            throw new UserAlreadyExitException("User already exists");
        }

        logger.debug("Création utilisateur avec rôle demandé : {}", request.getRole());
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        ERole role = ERole.fromString(request.getRole());
        Role userRole = roleRepository.findByName(role)
            .orElseThrow(() -> {
                logger.error("Rôle {} non trouvé en base", role);
                return new RoleNotFoundException("Rôle non trouvé");
            });

        logger.info("Rôle trouvé en base : {}", userRole.getName());
        
        user.getRoles().add(userRole);
        
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User updateUserRole(Long userId, ERole newRole) {
        // Récupération de l'admin actuellement authentifié
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'ID : " + userId));

        // Empêche un admin de modifier son propre rôle
        if (user.getUsername().equals(currentUsername)) {
            throw new SelfRoleModificationException("Un administrateur ne peut pas modifier son propre rôle");
        }

        Role role = roleRepository.findByName(newRole)
            .orElseThrow(() -> new RoleNotFoundException("Rôle non valide : " + newRole));

        // Suppression de tous les rôles existants (système à rôle unique)
        user.getRoles().clear();
        user.getRoles().add(role);

        return userRepository.save(user);
    }

    @Transactional
    public User updateUserProfile(Long userId, UserUpdateRequest request) {
        User currentUser = getCurrentAuthenticatedUser();
        User userToUpdate = getUserById(userId);

        validateUpdatePermission(currentUser, userToUpdate);

        updateUserFields(userToUpdate, request);

        return userRepository.save(userToUpdate);
    }

    private User getCurrentAuthenticatedUser() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(currentUsername)
            .orElseThrow(() -> new UserNotFoundException("Utilisateur authentifié non trouvé"));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("Utilisateur cible non trouvé"));
    }

    public void deleteUser(Long userId) {
        User currentUser = getCurrentAuthenticatedUser();
        if(currentUser.getId().equals(userId)) {
            throw new SelfDeletionException("Un administrateur ne peut pas se supprimer lui-même");
        }else{
            userRepository.deleteById(userId);
        }
    }

    private void validateUpdatePermission(User currentUser, User userToUpdate) {
        boolean isAdmin = currentUser.getRoles().stream()
            .anyMatch(role -> role.getName() == ERole.ROLE_ADMIN);
        
        if (!isAdmin && !currentUser.getId().equals(userToUpdate.getId())) {
            throw new AccessDeniedException("Vous ne pouvez modifier que votre propre profil");
        }
    }

    //get the user  role
    public String getUserRole(Long userId) {
        User user = userRepository.findById(userId)
           .orElseThrow(() -> new UserNotFoundException("User not found"));
            return user.getRoles().toString();}


    private void updateUserFields(User user, UserUpdateRequest request) {
        Optional.ofNullable(request.getEmail()).ifPresent(user::setEmail);
        Optional.ofNullable(request.getPhoneNumber()).ifPresent(user::setPhoneNumber);
        Optional.ofNullable(request.getPassword())
            .ifPresent(password -> user.setPassword(passwordEncoder.encode(password)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAllWithRoles(pageable);
    }

    @Override
    public User manageUserSubscription(Long userId, Long packId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'manageUserSubscription'");
    }

    public User findByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.orElseThrow(() -> new RuntimeException("User not found"));
    }


}
