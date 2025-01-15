package com.aluracursos.Foro.Hub.domain.usuario;
import com.aluracursos.Foro.Hub.domain.usuario.perfil.DatosPerfilRespuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    UserDetails findByCorreoElectronico(String username);

    Optional<Usuario> findUsuarioByCorreoElectronico(String correoElectronico);

    @Query("SELECT new com.aluracursos.Foro.Hub.domain.usuario.perfil.DatosPerfilRespuesta(u.id, u.nombre, u.perfil) FROM Usuario u")
    List<DatosPerfilRespuesta> findAllIdNombresYPerfiles();
}
