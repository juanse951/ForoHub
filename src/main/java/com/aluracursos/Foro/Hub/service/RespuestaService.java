package com.aluracursos.Foro.Hub.service;

import com.aluracursos.Foro.Hub.domain.respuesta.DatosRegistroRespuesta;
import com.aluracursos.Foro.Hub.domain.respuesta.Respuesta;
import com.aluracursos.Foro.Hub.domain.respuesta.RespuestaRepository;
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

        public Respuesta crearRespuesta(DatosRegistroRespuesta datos, Usuario autor, Topico topico) {
            String mensaje = (datos.mensaje() == null || datos.mensaje().isBlank()) ? "aún sin respuesta" : datos.mensaje();
            LocalDateTime fechaCreacion = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

            Respuesta respuesta =
                    new Respuesta(
                            null,
                            mensaje,
                            topico,
                            fechaCreacion,
                            autor,
                            null);

            return respuestaRepository.save(respuesta);
        }
    }

