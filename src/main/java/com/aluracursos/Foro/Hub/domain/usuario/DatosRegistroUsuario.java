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

    private static final Set<String> nombresExistentes = new HashSet<>();
    private static final Set<String> correosExistentes = new HashSet<>();
    private static final Random random = new Random();

    private static String generarCadenaRandom() {
        int randomInt = random.nextInt(10000);
        return String.format("%04d", randomInt);
    }

    private static String asegurarUnicidad(String valor, Set<String> existentes) {
        String unico = valor;
        while (existentes.contains(unico)) {
            unico = valor + generarCadenaRandom();
        }
        existentes.add(unico);
        return unico;
    }

    private static String convertirNombre(String nombre) {
        String nombreNormalizado = Normalizer.normalize(nombre, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[&=_'\\-+,.<>:@*]", "")
                .replaceAll("\\.{2,}", "")
                .replaceAll("\\s", "")
                .toLowerCase();
        return asegurarUnicidad(nombreNormalizado, nombresExistentes);
    }

    private static String convertirCorreo(String nombre) {
        String nombreUsuario = convertirNombre(nombre);
        String correo = nombreUsuario + "@example.com";
        return asegurarUnicidad(correo, correosExistentes);
    }

    public static Usuario registro(String nombre) {
        return new Usuario(
                null,
                convertirNombre(nombre),
                convertirCorreo(nombre),
                "defaultPassword",
                null,
                new ArrayList<>(),
                new ArrayList<>()
        );
    }
}
