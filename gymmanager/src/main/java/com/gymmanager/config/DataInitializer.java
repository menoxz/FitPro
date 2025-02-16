package com.gymmanager.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.gymmanager.model.ERole;
import com.gymmanager.model.Role;
import com.gymmanager.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        createRoleIfNotExists(ERole.ROLE_ADMIN);
        createRoleIfNotExists(ERole.ROLE_STAFF);
    }

    private void createRoleIfNotExists(ERole roleName) {

        if (!roleRepository.existsByName(roleName)) {
            roleRepository.save(new Role(roleName));
        }
    }
}
