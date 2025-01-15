package com.aluracursos.Foro.Hub.service;

import com.aluracursos.Foro.Hub.domain.usuario.*;
import com.aluracursos.Foro.Hub.domain.usuario.perfil.DatosActualizarPerfil;
import com.aluracursos.Foro.Hub.domain.usuario.perfil.DatosPerfilRespuesta;
import com.aluracursos.Foro.Hub.infra.exceptions.UsuarioNotFoundByIdException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public DatosRespuestaUsuario registrarUsuario(DatosRegistroUsuario datosRegistroUsuario) {

        String correoElectronico = datosRegistroUsuario.correoElectronico().trim().toLowerCase();

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

        if (usuarioRepository.count() == 0) {
            usuario.setPerfil(TipoPerfil.ADMIN);
        } else {
            usuario.setPerfil(TipoPerfil.USER);
        }

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
            String correoLimpio = datosActualizarUsuario.correoElectronico().trim().toLowerCase();

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
            String contrasena = datosActualizarUsuario.contrasena();
            if (!validarContrasena(contrasena)) {
                throw new ConstraintViolationException("La contraseña debe tener al menos una letra mayúscula, una letra minúscula, un número, un carácter especial, debe tener al menos 8 caracteres.NO Contener ESPACIOS, al principio en su longitud o al final", null);
            }
            String contrasenaHasheada = passwordEncoder.encode(contrasena);
            usuario.setContrasena(contrasenaHasheada);
        }

        return usuarioRepository.save(usuario);
    }

    public Page<Usuario> obtenerListadoUsuario(Pageable paginacion) {
        Pageable paginacionConOrden = PageRequest.of(paginacion.getPageNumber(), 10, Sort.by(Sort.Order.asc("nombre")));
        return usuarioRepository.findAll(paginacionConOrden);
    }

    public Usuario obtenerUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundByIdException("No se encontró el Usuario con ID " + id));
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        var optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isEmpty()) {
            throw new UsuarioNotFoundByIdException("No se encontró el Usuario con ID " + id);
        }
        usuarioRepository.deleteById(id);
    }

    private boolean validarContrasena(String contrasena) {
        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(contrasena);
        return matcher.matches();
    }

    private boolean esCorreoValido(String correo) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(correo);
        return matcher.matches();
    }

    public Page<DatosPerfilRespuesta> obtenerIdNombresYPerfiles(Pageable paginacion) {
        return usuarioRepository.findAll(paginacion)
                .map(usuario -> new DatosPerfilRespuesta(usuario));
    }


    @Transactional
    public Usuario actualizarPerfil(Long id, @Valid DatosActualizarPerfil datosActualizarPerfil) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El Usuario con ID " + id + " no existe."));

        if (datosActualizarPerfil.perfil() != null && !datosActualizarPerfil.perfil().name().trim().isEmpty()) {
            try {
                TipoPerfil perfilValido = TipoPerfil.fromString(datosActualizarPerfil.perfil().name());
                usuario.setPerfil(perfilValido);
            } catch (IllegalArgumentException e) {

                String perfilesDisponibles = Stream.of(TipoPerfil.values())
                        .map(TipoPerfil::getRole)
                        .collect(Collectors.joining(", "));

                throw new IllegalArgumentException("Perfil no válido: " + datosActualizarPerfil.perfil() +
                        ". Verifica los perfiles disponibles: " + perfilesDisponibles);
            }
        }
        return usuarioRepository.save(usuario);
    }

}
