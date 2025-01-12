package com.aluracursos.Foro.Hub.domain.usuario;

public enum TipoPerfil {
    USER("user"),
    ADMIN("admi");

    private final String descripcion;

    TipoPerfil(String descripcion) {
        this.descripcion = descripcion;
    }

    //para cuando se deba ingresar valores string para escoger status
//    public static TipoPerfil fromString(String nombre) {
//        for (TipoPerfil c : TipoPerfil.values()) {
//            if (c.name().equalsIgnoreCase(nombre)) {
//                return c;
//            }
//        }
//        return USER; // Valor por defecto en caso de que no se encuentre
//    }

}
