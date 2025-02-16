package com.gymmanager.model;

import java.util.Arrays;

public enum ERole {
    ROLE_STAFF,
    ROLE_ADMIN;

    public static ERole fromString(String roleName) {
        if (roleName == null || roleName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du rôle ne peut pas être vide");
        }

        String normalized = roleName.trim().toUpperCase();

        // Gère les cas où le préfixe ROLE_ est omis
        if (!normalized.startsWith("ROLE_")) {
            normalized = "ROLE_" + normalized;
        }

        for (ERole role : ERole.values()) {
            if (role.name().equals(normalized)) {
                return role;
            }
        }

        throw new IllegalArgumentException("Rôle invalide : '" + roleName 
            + "'. Rôles valides : " + Arrays.toString(ERole.values()));
    }
}
