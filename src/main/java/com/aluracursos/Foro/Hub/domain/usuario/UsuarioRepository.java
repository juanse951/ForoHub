package com.aluracursos.Foro.Hub.domain.usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNombre(String nombre);

    boolean existsByCorreoElectronico(String correoElectronico);
    boolean existsByNombre(String nombre);

    UserDetails findByCorreoElectronico(String username);
}
