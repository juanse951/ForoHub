package com.aluracursos.Foro.Hub.domain.curso;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

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
        for (Categoria categoria : Categoria.values()) {
            if (categoria.name().equalsIgnoreCase(value.replace(" ", "_"))) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Categoría no válida: " + value);
    }

    @JsonValue
    public String getDescripcion() {
        return descripcion;
    }
}


