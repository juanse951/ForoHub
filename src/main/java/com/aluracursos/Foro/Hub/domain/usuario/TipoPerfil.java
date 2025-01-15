package com.aluracursos.Foro.Hub.domain.usuario;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.text.Normalizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum TipoPerfil {
    USER("USER"),
    ADMIN("ADMIN"),
    MODERATOR("MODERATOR");

    private final String role;

    TipoPerfil(String role) {
        this.role = role;
    }

    @JsonCreator
    public static TipoPerfil fromString(String value) {
        String cleanedValue = normalizeString(value.trim());

        if (cleanedValue.isEmpty()) {
            return null;
        }

        for (TipoPerfil perfil : TipoPerfil.values()) {
            if (perfil.name().equalsIgnoreCase(cleanedValue)) {
                return perfil;
            }
        }

        String perfilesDisponibles = Stream.of(TipoPerfil.values())
                .map(TipoPerfil::getRole)
                .collect(Collectors.joining(", "));
        throw new IllegalArgumentException("Tipo de perfil no válido: '" + value + "'. Perfiles disponibles: " + perfilesDisponibles);
    }

    @JsonValue
    public String getRole() {
        return role;
    }

    private static String normalizeString(String value) {
        return Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .toLowerCase();
    }
}

