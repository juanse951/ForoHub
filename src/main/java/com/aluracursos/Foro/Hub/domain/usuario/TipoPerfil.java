package com.aluracursos.Foro.Hub.domain.usuario;

import lombok.Getter;

public enum TipoPerfil {
    USER("USER"),
    ADMIN("ADMIN"),
    MODERATOR("MODERATOR");

    @Getter
    private final String role;

    TipoPerfil(String role) {
        this.role = role;
    }
}

