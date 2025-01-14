package com.aluracursos.Foro.Hub.service;

import com.aluracursos.Foro.Hub.domain.respuesta.*;
import com.aluracursos.Foro.Hub.domain.topico.Topico;
import com.aluracursos.Foro.Hub.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class RespuestaService {

        @Autowired
        private RespuestaRepository respuestaRepository;

        @Autowired
        private UsuarioService usuarioService;

        public DatosRespuestaRespuesta registroRespuesta(DatosRegistroRespuesta datosRegistroRespuesta) {
            String mensaje = datosRegistroRespuesta.mensaje().trim().toLowerCase();
            LocalDateTime fechaCreacion = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            RespuestaStatus respuestaStatus = RespuestaStatus.PENDIENTE;


            Topico topico = new Topico();
            topico.setId(datosRegistroRespuesta.topico_id());

            Usuario autor = usuarioService.obtenerUsuario(datosRegistroRespuesta.autor_id());


            Respuesta respuesta = new Respuesta(datosRegistroRespuesta, topico ,autor);
            respuesta.setMensaje(mensaje);
            respuesta.setFechaCreacion(fechaCreacion);
            respuesta.setSolucion(respuestaStatus);

            respuestaRepository.save(respuesta);

            return new DatosRespuestaRespuesta(
                    respuesta.getId(),
                    respuesta.getMensaje(),
                    respuesta.getTopico().getTitulo(),
                    respuesta.getFechaCreacion(),
                    respuesta.getAutor().getNombre(),
                    respuesta.getSolucion().toString()
            );
        }
    }

