package com.aluracursos.Foro.Hub.service;

import com.aluracursos.Foro.Hub.domain.usuario.*;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public DatosRespuestaUsuario registrarUsuario(DatosRegistroUsuario datosRegistroUsuario) {

        String correoElectronico = datosRegistroUsuario.correoElectronico().trim();

        Optional<Usuario> usuarioExistente = usuarioRepository.findUsuarioByCorreoElectronico(correoElectronico);
        if (usuarioExistente.isPresent()) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado.");
        }

        String nombreLimpio = datosRegistroUsuario.nombre().trim();

        if (!esCorreoValido(correoElectronico)) {
            throw new ConstraintViolationException("El correo electrónico ingresado no es válido. Asegúrate de que tenga un formato correcto, como usuario@forohub.com", null);
        }

        Usuario usuario = new Usuario(datosRegistroUsuario);
        usuario.setNombre(nombreLimpio);
        usuario.setCorreoElectronico(correoElectronico);
        String contrasenaHasheada = passwordEncoder.encode(datosRegistroUsuario.contrasena());
        usuario.setContrasena(contrasenaHasheada);
        usuarioRepository.save(usuario);

        return new DatosRespuestaUsuario(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreoElectronico(),
                usuario.getPerfil()
        );
    }

    @Transactional
    public Usuario actualizarUsuario(Long id, DatosActualizarUsuario datosActualizarUsuario) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del usuario debe ser un número positivo.");
        }

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El usuario con ID " + id + " no existe."));

        if (datosActualizarUsuario.nombre() != null && !datosActualizarUsuario.nombre().trim().isEmpty()) {
            String nombreLimpio = datosActualizarUsuario.nombre().trim();
            usuario.setNombre(nombreLimpio);
        }

        if (datosActualizarUsuario.correoElectronico() != null && !datosActualizarUsuario.correoElectronico().trim().isEmpty()) {
            String correoLimpio = datosActualizarUsuario.correoElectronico().trim();

            // Verificar si el correo pertenece a otro usuario
            Optional<Usuario> usuarioConCorreo = usuarioRepository.findUsuarioByCorreoElectronico(correoLimpio);
            if (usuarioConCorreo.isPresent() && !usuarioConCorreo.get().getId().equals(usuario.getId())) {
                throw new IllegalArgumentException("El correo electrónico ingresado ya está registrado.");
            }

            if (!esCorreoValido(correoLimpio)) {
                throw new ConstraintViolationException("El correo electrónico ingresado no es válido. Asegúrate de que tenga un formato correcto, como usuario@forohub.com", null);
            }
            usuario.setCorreoElectronico(correoLimpio);
        }

        if (datosActualizarUsuario.contrasena() != null && !datosActualizarUsuario.contrasena().trim().isEmpty()) {
            String contrasenaHasheada = passwordEncoder.encode(datosActualizarUsuario.contrasena());
            usuario.setContrasena(contrasenaHasheada);
        }

        return usuarioRepository.save(usuario);
    }

    private boolean esCorreoValido(String correo) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(correo);
        return matcher.matches();
    }

}
