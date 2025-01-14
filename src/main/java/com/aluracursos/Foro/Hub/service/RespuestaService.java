package com.aluracursos.Foro.Hub.service;

import com.aluracursos.Foro.Hub.domain.respuesta.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class RespuestaService {

        @Autowired
        private RespuestaRepository respuestaRepository;

        public DatosRespuestaRespuesta registroRespuesta(DatosRegistroRespuesta datosRegistroRespuesta) {
            String mensaje = datosRegistroRespuesta.mensaje().trim().toLowerCase();
            LocalDateTime fechaCreacion = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            RespuestaStatus respuestaStatus = RespuestaStatus.PENDIENTE;

            Respuesta respuesta = new Respuesta(datosRegistroRespuesta);
            respuesta.setMensaje(mensaje);
            respuesta.setFechaCreacion(fechaCreacion);
            respuesta.setSolucion(respuestaStatus);

            respuestaRepository.save(respuesta);

            return new DatosRespuestaRespuesta(
                    respuesta.getId(),
                    respuesta.getMensaje(),
                    respuesta.getFechaCreacion(),
                    respuesta.getSolucion()
            );
        }
    }

