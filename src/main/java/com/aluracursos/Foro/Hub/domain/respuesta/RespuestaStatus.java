package com.aluracursos.Foro.Hub.domain.respuesta;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.text.Normalizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum RespuestaStatus {

    PENDIENTE("Pendiente"),
    NO_SOLUCIONADO("No solucionado"),
    SOLUCIONADO("Solucionado");

    private final String descripcion;

    RespuestaStatus(String descripcion) {
        this.descripcion = descripcion;
    }

    @JsonCreator
    public static RespuestaStatus fromString(String value) {
        String cleanedValue = normalizeString(value.trim());

        if (cleanedValue.isEmpty()) {
            return null;
        }

        for (RespuestaStatus status : RespuestaStatus.values()) {
            if (status.name().equalsIgnoreCase(cleanedValue.replace(" ", "_"))) {
                return status;
            }
        }

        String statusesDisponibles = Stream.of(RespuestaStatus.values())
                .map(RespuestaStatus::getDescripcion)
                .collect(Collectors.joining(", "));
        throw new IllegalArgumentException("Estado de respuesta no válido: '" + value + "'. Estados disponibles: " + statusesDisponibles);
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
