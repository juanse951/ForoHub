package com.aluracursos.Foro.Hub.domain.curso;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.text.Normalizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Categoria {

    GENERAL("General"),
    PROGRAMACION("Programación"),
    DESARROLLO_WEB("Desarrollo Web"),
    DESARROLLO_SOFTWARE("Desarrollo Software"),
    BASE_DE_DATOS("Base de Datos"),
    REDES_Y_SEGURIDAD("Redes y Seguridad"),
    IA_Y_ML("IA y ML"),
    CLOUD_COMPUTING("Cloud Computing"),
    HARDWARE("Hardware"),
    INNOVACIONES_TECNOLOGICAS("Innovaciones Tecnológicas"),
    GAMIFICACION_Y_VIDEOJUEGOS("Gamificación y Videojuegos"),
    SOPORTE_TECNICO("Soporte Técnico"),
    EDUCACION_EN_TECNOLOGIA("Educación en Tecnología"),
    TECNOLOGIA_Y_SOCIEDAD("Tecnología y Sociedad");

    private final String descripcion;

    Categoria(String descripcion) {
        this.descripcion = descripcion;
    }

    @JsonCreator
    public static Categoria fromString(String value) {

        String cleanedValue = normalizeString(value.trim());

        if (cleanedValue.isEmpty()) {
            return null;
        }

        for (Categoria categoria : Categoria.values()) {
            if (categoria.name().equalsIgnoreCase(cleanedValue.replace(" ", "_"))) {
                return categoria;
            }
        }
        String categoriasDisponibles = Stream.of(Categoria.values())
                .map(Categoria::getDescripcion)
                .collect(Collectors.joining(", "));
        throw new IllegalArgumentException("Categoría no válida: '" + value + "'. Categorías disponibles: " + categoriasDisponibles);
    }

    @JsonValue
    public String getDescripcion() {
        return descripcion;
    }

    private static String normalizeString(String value) {
        return Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .toLowerCase();
    }
}


