package com.aluracursos.Foro.Hub.domain.topico;

public enum TopicoStatus {

    ACTIVO("activo"),
    INACTIVO("inactivo"),
    PENDIENTE("pendiente");

    private final String descripcion;

    TopicoStatus(String descripcion) {
        this.descripcion = descripcion;
    }

}


