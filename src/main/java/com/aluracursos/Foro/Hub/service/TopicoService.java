package com.aluracursos.Foro.Hub.service;

import com.aluracursos.Foro.Hub.domain.curso.Curso;
import com.aluracursos.Foro.Hub.domain.curso.CursoRepository;
import com.aluracursos.Foro.Hub.domain.curso.DatosRegistroCurso;
import com.aluracursos.Foro.Hub.domain.respuesta.DatosRegistroRespuesta;
import com.aluracursos.Foro.Hub.domain.respuesta.Respuesta;
import com.aluracursos.Foro.Hub.domain.respuesta.RespuestaRepository;
import com.aluracursos.Foro.Hub.domain.topico.*;
import com.aluracursos.Foro.Hub.domain.usuario.DatosRegistroUsuario;
import com.aluracursos.Foro.Hub.domain.usuario.Usuario;
import com.aluracursos.Foro.Hub.domain.usuario.UsuarioRepository;
import com.aluracursos.Foro.Hub.domain.usuario.perfil.DatosRegistroPerfil;
import com.aluracursos.Foro.Hub.domain.usuario.perfil.Perfil;
import com.aluracursos.Foro.Hub.domain.usuario.perfil.PerfilRepository;
import com.aluracursos.Foro.Hub.infra.exceptions.TopicoAlreadyExistsException;

import com.aluracursos.Foro.Hub.infra.exceptions.TopicoNotFoundByIdException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private RespuestaService respuestaService;

    @Autowired
    private CursoService cursoService;

    @Transactional
    public Topico crearTopico(DatosRegistroTopico datosRegistroTopico) {

        boolean existeTitulo = topicoRepository.existsByTitulo(datosRegistroTopico.titulo());
        if (existeTitulo) {
            throw new TopicoAlreadyExistsException("El título del tópico ya existe.");
        }

        boolean existeMensaje = topicoRepository.existsByMensaje(datosRegistroTopico.mensaje());
        if (existeMensaje) {
            throw new TopicoAlreadyExistsException("El mensaje del tópico ya existe.");
        }

        Curso curso = cursoRepository.findByNombre(datosRegistroTopico.curso())
                .orElseGet(() -> cursoService.crearCurso(new DatosRegistroCurso(datosRegistroTopico.curso())));

        Usuario autor = usuarioRepository.findByNombre(datosRegistroTopico.autor())
                .orElseGet(() -> {
                    Usuario nuevoUsuario = DatosRegistroUsuario.registro(datosRegistroTopico.autor(),usuarioRepository);

                    Perfil perfil = DatosRegistroPerfil.registro(null);
                    perfil = perfilRepository.save(perfil);
                    nuevoUsuario.setPerfil(List.of(perfil));

                    return usuarioRepository.save(nuevoUsuario);
                });

        var topico = topicoRepository.save(new Topico(
                datosRegistroTopico,
                curso,
                autor,
                new ArrayList<>()))
                ;

        Respuesta respuesta = respuestaService.crearRespuesta(new DatosRegistroRespuesta(null), autor, topico);
        topico.getRespuestas().add(respuesta);

        return topico;
    }

    @Transactional
    public Topico actualizarTopico(Long id, DatosActualizarTopico datosActualizarTopico) {
        var optionalTopico = topicoRepository.findById(id);
        if (optionalTopico.isEmpty()) {
            throw new TopicoNotFoundByIdException("No se encontró el tópico con ID " + id);
        }

        Topico topico = optionalTopico.get();

        // Actualización de los datos del tópico
        if (datosActualizarTopico.titulo() != null && !datosActualizarTopico.titulo().trim().isEmpty()) {
            topico.setTitulo(datosActualizarTopico.titulo());
        }
        if (datosActualizarTopico.mensaje() != null && !datosActualizarTopico.mensaje().trim().isEmpty()) {
            topico.setMensaje(datosActualizarTopico.mensaje());
        }
        if (datosActualizarTopico.autor() != null
                && datosActualizarTopico.autor().nombre() != null
                && !datosActualizarTopico.autor().nombre().trim().isEmpty()) {
            topico.getAutor().actualizarDatos(datosActualizarTopico.autor());
        }
        if (datosActualizarTopico.curso() != null
                && datosActualizarTopico.curso().nombre() != null
                && !datosActualizarTopico.curso().nombre().trim().isEmpty()) {
            Curso cursoActualizado = cursoService.actualizarCurso(topico.getCurso().getId(), datosActualizarTopico.curso());
            topico.setCurso(cursoActualizado);
        }

        return topicoRepository.save(topico);
    }

    @Transactional
    public void eliminarTopico(Long id) {
        var optionalTopico = topicoRepository.findById(id);
        if (optionalTopico.isEmpty()) {
            throw new TopicoNotFoundByIdException("No se encontró el tópico con ID " + id);
        }
        topicoRepository.deleteById(id);
    }

    public Topico obtenerTopico(Long id) {
        return topicoRepository.findById(id)
                .orElseThrow(() -> new TopicoNotFoundByIdException("No se encontró el tópico con ID " + id));
    }

    public Page<Topico> obtenerListadoTopicos(Pageable paginacion) {
        Pageable paginacionConOrden = PageRequest.of(paginacion.getPageNumber(), 10, Sort.by(Sort.Order.desc("fechaCreacion")));
        return topicoRepository.findAll(paginacionConOrden);
    }
}
