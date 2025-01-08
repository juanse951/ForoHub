package com.aluracursos.Foro.Hub.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.text.Normalizer;
import java.util.ArrayList;

public record DatosRegistroUsuario(

        @NotBlank(message = "{nombre.obligatorio}")
        String nombre,

        @NotBlank(message = "{email.obligatorio}")
        @Email(message = "{email.invalido}")
        String correoElectronico,

        @NotBlank(message = "{password.obligatorio}")
        @Pattern(regexp = "^(defaultPassword|(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,})$",
                message = "{password.invalido}")
        String contrasena
) {

    private static String convertirNombre(String nombre) {

        // Eliminar tildes y caracteres especiales
        String nombreNormalizado = Normalizer.normalize(nombre, Normalizer.Form.NFD);
        nombreNormalizado = nombreNormalizado.replaceAll("[^\\p{ASCII}]", ""); // Elimina caracteres no ASCII

        // Eliminar caracteres no permitidos: &, =, _, ', -, +, ',', <, >, :, @, *
        nombreNormalizado = nombreNormalizado.replaceAll("[&=_'\\-+,.<>:@*]", ""); // Excluye también @ y *
        nombreNormalizado = nombreNormalizado.replaceAll("\\.{2,}", ""); // Elimina puntos dobles consecutivos

        // Eliminar espacios
        nombreNormalizado = nombreNormalizado.replaceAll("\\s", "");

        // Convertir a minúsculas
        nombreNormalizado = nombreNormalizado.toLowerCase();

        return nombreNormalizado;
    }

    private static String convertirCorreo(String nombre) {

        String nombreUsuario = convertirNombre(nombre);

        // Agregar un dominio genérico para construir el correo
        return nombreUsuario + "@example.com";
    }

    public static Usuario registro(String nombre){

        return new Usuario(null,
                convertirNombre(nombre),
                convertirCorreo(nombre),
                "defaultPassword",
                null,
                new ArrayList<>(),
                new ArrayList<>());
    }
}
