package com.gymmanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gymmanager.model.ERole;
import com.gymmanager.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
    boolean existsByName(ERole name);
    
    
}