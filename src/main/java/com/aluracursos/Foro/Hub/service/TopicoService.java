package com.aluracursos.Foro.Hub.service;

import com.aluracursos.Foro.Hub.domain.curso.Curso;
import com.aluracursos.Foro.Hub.domain.curso.CursoRepository;
import com.aluracursos.Foro.Hub.domain.respuesta.RespuestaRepository;
import com.aluracursos.Foro.Hub.domain.topico.*;
import com.aluracursos.Foro.Hub.domain.usuario.Usuario;
import com.aluracursos.Foro.Hub.domain.usuario.UsuarioRepository;
import com.aluracursos.Foro.Hub.infra.exceptions.*;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
    private RespuestaService respuestaService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private UsuarioService usuarioService;

    @Transactional
    public Topico crearTopico(DatosRegistroTopico datosRegistroTopico) {

        String tituloLimpio = datosRegistroTopico.titulo() != null ? datosRegistroTopico.titulo().trim() : null;
        String mensajeLimpio = datosRegistroTopico.mensaje() != null ? datosRegistroTopico.mensaje().trim() : null;

        if (topicoRepository.existsByTitulo(tituloLimpio)) {
            throw new TopicoAlreadyExistsException("El título del tópico ya existe.");
        }

        if (topicoRepository.existsByMensaje(mensajeLimpio)) {
            throw new TopicoAlreadyExistsException("El mensaje del tópico ya existe.");
        }

        Curso curso = cursoService.obtenerCurso(datosRegistroTopico.curso_id());
        Usuario autor = usuarioService.obtenerUsuario(datosRegistroTopico.autor_id());

        var topico = topicoRepository.save(
                new Topico(
                        new DatosRegistroTopico(tituloLimpio, mensajeLimpio, datosRegistroTopico.curso_id(), datosRegistroTopico.autor_id()),
                        curso,
                        autor,
                        new ArrayList<>()
                )
        );

//        Respuesta respuesta = respuestaService.crearRespuesta
//                (new DatosRegistroRespuesta
//                        (null),
//                        autor,
//                        topico);
//        topico.getRespuestas().add(respuesta);

        return topico;
    }

    @Transactional
    public Topico actualizarTopico(Long id, DatosActualizarTopico datosActualizarTopico) {

        Topico topico = obtenerTopico(id);

        if (datosActualizarTopico.titulo() != null && !datosActualizarTopico.titulo().trim().isEmpty()) {
            topico.setTitulo(datosActualizarTopico.titulo());
        }
        if (datosActualizarTopico.mensaje() != null && !datosActualizarTopico.mensaje().trim().isEmpty()) {
            topico.setMensaje(datosActualizarTopico.mensaje());
        }
        // Validar y actualizar el autor
        if (datosActualizarTopico.autor_id() != null && datosActualizarTopico.autor_id() > 0) {
            var optionalAutor = usuarioRepository.findById(datosActualizarTopico.autor_id());
            if (optionalAutor.isEmpty()) {
                throw new UsuarioNotFoundException("El autor con ID " + datosActualizarTopico.autor_id() + " no existe.");
            }
            topico.setAutor(optionalAutor.get());
        }

        // Validar y actualizar el curso
        if (datosActualizarTopico.curso_id() != null && datosActualizarTopico.curso_id() > 0) {
            var optionalCurso = cursoRepository.findById(datosActualizarTopico.curso_id());
            if (optionalCurso.isEmpty()) {
                throw new CursoNotFoundException("El curso con ID " + datosActualizarTopico.curso_id() + " no existe.");
            }
            topico.setCurso(optionalCurso.get());
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
