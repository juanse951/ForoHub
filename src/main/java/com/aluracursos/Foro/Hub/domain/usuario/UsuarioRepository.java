package com.aluracursos.Foro.Hub.domain.usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    UserDetails findByCorreoElectronico(String username);

    Optional<Usuario> findUsuarioByCorreoElectronico(String correoElectronico);

    List<Usuario> findByPerfil(TipoPerfil perfil);
}
