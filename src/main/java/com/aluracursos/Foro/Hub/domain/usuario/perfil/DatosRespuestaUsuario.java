package com.aluracursos.Foro.Hub.domain.usuario.perfil;

import com.aluracursos.Foro.Hub.domain.usuario.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.text.Normalizer;
import java.util.ArrayList;

public record DatosRespuestaUsuario(

        @NotBlank(message = "{nombre.obligatorio}")
        String nombre,

        @NotBlank(message = "{email.obligatorio}")
        @Email(message = "{email.invalido}")
        String correoElectronico,

        @NotBlank(message = "{password.obligatorio}")
        @Pattern(
                regexp = "^(defaultPassword|(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,})$",
                message = "{password.invalido}")
        String contrasena
) {
    private static String convertirCorreo(String nombre) {
        // Eliminar tildes y caracteres especiales
        String nombreNormalizado = Normalizer.normalize(nombre, Normalizer.Form.NFD);
        nombreNormalizado = nombreNormalizado.replaceAll("[^\\p{ASCII}]", ""); // Elimina caracteres no ASCII

        // Eliminar caracteres no permitidos para un correo (como @* que no son válidos)
        nombreNormalizado = nombreNormalizado.replaceAll("[^a-zA-Z0-9._%+-]", "");  // Permite letras, números, '.', '_', '%', '+', '-'

        // Convertir el nombre a correo electrónico
        return nombreNormalizado.replaceAll(" ", "") + "@example.com";
    }

    public static Usuario respuesta(String nombre){
        String correoElectronico = convertirCorreo(nombre);
        String contrasena = "defaultPassword";
        return new Usuario(null, nombre, correoElectronico, contrasena,
                null, new ArrayList<>(),new ArrayList<>());
    }
}
