package com.aluracursos.Foro.Hub.service;

import com.aluracursos.Foro.Hub.domain.usuario.DatosActualizarUsuario;
import com.aluracursos.Foro.Hub.domain.usuario.DatosRegistroUsuario;
import com.aluracursos.Foro.Hub.domain.usuario.Usuario;
import com.aluracursos.Foro.Hub.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Random;

@Service
public class UsuarioService {

        @Autowired
        private UsuarioRepository usuarioRepository;

        private static final Random random = new Random();

        private static String generarCadenaRandom() {
            int randomInt = random.nextInt(10000);
            return String.format("%04d", randomInt);
        }

    private static String asegurarUnicidad(String correo, UsuarioRepository usuarioRepository) {
        String correoUnico = correo;
        while (usuarioRepository.existsByCorreoElectronico(correoUnico)) {
            String base = correo.split("@")[0];
            correoUnico = base + generarCadenaRandom() + "@ForoHub.com";
        }
        return correoUnico;
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
            String correoBase = nombreUsuario + "@ForoHub.com";
            String correoUnico = asegurarUnicidad(correoBase, usuarioRepository);

            return correoUnico;
        }

    public Usuario actualizarUsuario(Long id, DatosActualizarUsuario datosActualizarUsuario) {
        Usuario usuario = usuarioRepository.findById(id).orElse(new Usuario());

        if (datosActualizarUsuario.nombre() != null && !datosActualizarUsuario.nombre().trim().isEmpty()) {
            usuario.setNombre(datosActualizarUsuario.nombre());
        }

        return usuarioRepository.save(usuario);
    }


        public Usuario crearUsuario(DatosRegistroUsuario datosRegistroUsuario) {
            String correoTemporal = generarCorreoTemporal(datosRegistroUsuario.nombre(), usuarioRepository);
            String contrasena = "defaultPassword";

            Usuario usuario = new Usuario(
                    null,  // ID será asignado por la base de datos
                    datosRegistroUsuario.nombre(),
                    correoTemporal,
                    contrasena,
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>()
            );

            return usuarioRepository.save(usuario);
        }
    }
