package com.aluracursos.Foro.Hub.domain.topico;

public enum TopicoStatus {

    ACTIVO("Tópico activo"),
    INACTIVO("Tópico inactivo"),
    PENDIENTE("Tópico pendiente");

    private final String descripcion;

    TopicoStatus(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

}


