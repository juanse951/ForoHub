package com.aluracursos.Foro.Hub.domain.topico;

public enum TopicoStatus {

    ACTIVO("activo"),
    INACTIVO("inactivo"),
    PENDIENTE("pendiente");

    private final String descripcion;

    TopicoStatus(String descripcion) {
        this.descripcion = descripcion;
    }

    //para cuando se deba ingresar valores string para escoger status
//    public static TopicoStatus fromString(String status) {
//        for (TopicoStatus c : TopicoStatus.values()) {
//            if (c.name().equalsIgnoreCase(status)) {
//                return c;
//            }
//        }
//        return ACTIVO; // Valor por defecto en caso de que no se encuentre
//    }

}


