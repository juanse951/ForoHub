package com.aluracursos.Foro.Hub.service;

import com.aluracursos.Foro.Hub.domain.respuesta.*;
import com.aluracursos.Foro.Hub.domain.topico.Topico;
import com.aluracursos.Foro.Hub.domain.usuario.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

        Usuario autor = usuarioService.obtenerUsuario(datosRegistroRespuesta.autor_id());

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
}


