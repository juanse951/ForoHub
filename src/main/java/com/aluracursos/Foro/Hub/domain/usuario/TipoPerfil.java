package com.aluracursos.Foro.Hub.domain.usuario;

import lombok.Getter;

public enum TipoPerfil {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    MODERATOR("ROLE_MODERATOR");

    @Getter
    private final String role;

    TipoPerfil(String role) {
        this.role = role;
    }
}

