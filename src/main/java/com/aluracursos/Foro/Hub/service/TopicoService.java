package com.aluracursos.Foro.Hub.service;

import com.aluracursos.Foro.Hub.domain.curso.Curso;
import com.aluracursos.Foro.Hub.domain.curso.CursoRepository;
import com.aluracursos.Foro.Hub.domain.respuesta.DatosRegistroRespuesta;
import com.aluracursos.Foro.Hub.domain.respuesta.Respuesta;
import com.aluracursos.Foro.Hub.domain.respuesta.RespuestaRepository;
import com.aluracursos.Foro.Hub.domain.topico.*;
import com.aluracursos.Foro.Hub.domain.usuario.Usuario;
import com.aluracursos.Foro.Hub.domain.usuario.UsuarioRepository;
import com.aluracursos.Foro.Hub.infra.exceptions.CursoNotFoundException;
import com.aluracursos.Foro.Hub.infra.exceptions.TopicoAlreadyExistsException;

import com.aluracursos.Foro.Hub.infra.exceptions.TopicoNotFoundByIdException;
import com.aluracursos.Foro.Hub.infra.exceptions.UsuarioNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        Curso curso = new Curso();
        Usuario autor = new Usuario();

        boolean existeTitulo = topicoRepository.existsByTitulo(datosRegistroTopico.titulo());
        if (existeTitulo) {
            throw new TopicoAlreadyExistsException("El título del tópico ya existe.");
        }

        boolean existeMensaje = topicoRepository.existsByMensaje(datosRegistroTopico.mensaje());
        if (existeMensaje) {
            throw new TopicoAlreadyExistsException("El mensaje del tópico ya existe.");
        }

        Optional<Curso> cursoOptional = cursoRepository.findById(datosRegistroTopico.curso_id());
        if(cursoOptional.isPresent()){
             curso = cursoOptional.get();
        }

        Optional<Usuario> autorOptional = usuarioRepository.findById(datosRegistroTopico.autor_id());
                if(autorOptional.isPresent()){
                    autor = autorOptional.get();
                }

        var topico = topicoRepository.save(
                new Topico(
                datosRegistroTopico,
                curso,
                autor,
                new ArrayList<>())
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
        var optionalTopico = topicoRepository.findById(id);
        if (optionalTopico.isEmpty()) {
            throw new TopicoNotFoundByIdException("No se encontró el tópico con ID " + id);
        }

        Topico topico = optionalTopico.get();

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
