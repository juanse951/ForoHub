package com.aluracursos.Foro.Hub.service;

import com.aluracursos.Foro.Hub.domain.respuesta.*;
import com.aluracursos.Foro.Hub.domain.topico.Topico;
import com.aluracursos.Foro.Hub.domain.usuario.Usuario;
import com.aluracursos.Foro.Hub.infra.exceptions.RespuestaNotFoundByIdException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RespuestaService {

        @Autowired
        private RespuestaRepository respuestaRepository;

        @Autowired
        private UsuarioService usuarioService;

        @Autowired
        private TopicoService topicoService;


    @Transactional
    public Respuesta agregarRespuesta(Long topicoId, DatosRegistroRespuesta datosRegistroRespuesta) {

        Topico topico = topicoService.obtenerTopico(topicoId);

        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario autor = usuarioService.obtenerUsuarioPorEmail(emailUsuario);

        String mensaje = datosRegistroRespuesta.mensaje().trim();

        Respuesta respuesta = new Respuesta();
        respuesta.setMensaje(mensaje);
        respuesta.setFechaCreacion(LocalDateTime.now());
        respuesta.setSolucion(RespuestaStatus.PENDIENTE);
        respuesta.setAutor(autor);
        respuesta.setTopico(topico);

        topico.agregarRespuesta(respuesta);

        return respuestaRepository.save(respuesta);
    }


    @Transactional
    public Respuesta actualizarRespuesta(Long respuestaId, DatosActualizarRespuesta datosActualizarRespuesta) {
        if (respuestaId == null || respuestaId <= 0) {
            throw new IllegalArgumentException("El ID de la respuesta debe ser un número positivo.");
        }

        Respuesta respuesta = respuestaRepository.findById(respuestaId)
                .orElseThrow(() -> new IllegalArgumentException("La respuesta con ID " + respuestaId + " no existe."));

        if (datosActualizarRespuesta.mensaje() != null && !datosActualizarRespuesta.mensaje().trim().isEmpty()) {
            String mensajeLimpio = datosActualizarRespuesta.mensaje().trim();
            respuesta.setMensaje(mensajeLimpio);
        }

        if (datosActualizarRespuesta.solucion() != null) {
            respuesta.setSolucion(datosActualizarRespuesta.solucion());
        }

        respuesta.setFechaCreacion(LocalDateTime.now());

        return respuestaRepository.save(respuesta);
    }

    public Page<Respuesta> obtenerListadoRespuesta(Pageable paginacion) {
        Pageable paginacionConOrden = PageRequest.of(paginacion.getPageNumber(), 10, Sort.by(Sort.Order.asc("fechaCreacion")));
        return respuestaRepository.findAll(paginacionConOrden);
    }

    public Respuesta obtenerRespuesta(Long id) {
        return respuestaRepository.findById(id)
                .orElseThrow(() -> new RespuestaNotFoundByIdException("No se encontró la Respuesta con ID " + id));
    }

    @Transactional
    public void eliminarRespuesta(Long id) {
        var optionalRespuesta = respuestaRepository.findById(id);
        if (optionalRespuesta.isEmpty()) {
            throw new RespuestaNotFoundByIdException("No se encontró la Respuesta con ID " + id);
        }
        respuestaRepository.deleteById(id);
    }

}



