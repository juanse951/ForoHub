package com.aluracursos.Foro.Hub.domain.curso;

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

    //para cuando se deba ingresar valores string para escoger cat
//    public static Categoria fromString(String categoria) {
//        for (Categoria c : Categoria.values()) {
//            if (c.name().equalsIgnoreCase(categoria)) {
//                return c;
//            }
//        }
//        return GENERAL; // Valor por defecto en caso de que no se encuentre
//    }
}
