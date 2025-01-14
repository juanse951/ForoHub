package com.aluracursos.Foro.Hub.domain.respuesta;

public enum RespuestaStatus {

    PENDIENTE("Pendiente"),
    NO_SOLUCIONADO("No solucionado"),
    SOLUCIONADO("Solucionado");

    private final String descripcion;

    RespuestaStatus(String descripcion) {
        this.descripcion = descripcion;
    }
}
