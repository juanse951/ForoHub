package com.aluracursos.Foro.Hub.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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

    private static final Random random = new Random();

    private static String generarCadenaRandom() {
        int randomInt = random.nextInt(10000);
        return String.format("%04d", randomInt);
    }

    private static String asegurarUnicidad(String valor, UsuarioRepository usuarioRepository) {
        String unico = valor;
        while (usuarioRepository.existsByCorreoElectronico(unico) || usuarioRepository.existsByNombre(unico)) {
            unico = valor + generarCadenaRandom();
        }
        return unico;
    }

    private static String convertirNombreParaCorreo(String nombre) {
        String nombreNormalizado = Normalizer.normalize(nombre, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[&=_'\\-+,.<>:@*]", "")
                .replaceAll("\\.{2,}", "")
                .replaceAll("\\s", "")
                .toLowerCase();
        return nombreNormalizado;
    }

    private static String generarCorreoTemporal(String nombre, UsuarioRepository usuarioRepository) {
        String nombreUsuario = convertirNombreParaCorreo(nombre);
        String correoTemporal = nombreUsuario + (usuarioRepository.existsByCorreoElectronico(nombreUsuario) ? generarCadenaRandom() : "") + "@ForoHub.com";
        return asegurarUnicidad(correoTemporal, usuarioRepository);
    }

    public static Usuario registro(String nombre, UsuarioRepository usuarioRepository) {
        String correoTemporal = generarCorreoTemporal(nombre, usuarioRepository);

        return new Usuario(
                null,
                nombre,
                correoTemporal,
                "defaultPassword",
                null,
                new ArrayList<>(),
                new ArrayList<>()
        );
    }
}