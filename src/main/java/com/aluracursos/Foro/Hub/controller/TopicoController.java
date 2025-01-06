package com.aluracursos.Foro.Hub.controller;

import com.aluracursos.Foro.Hub.domain.curso.Curso;
import com.aluracursos.Foro.Hub.domain.curso.CursoRepository;
import com.aluracursos.Foro.Hub.domain.respuesta.Respuesta;
import com.aluracursos.Foro.Hub.domain.topico.DatosRegistroTopico;
import com.aluracursos.Foro.Hub.domain.topico.DatosRespuestaTopico;
import com.aluracursos.Foro.Hub.domain.topico.Topico;
import com.aluracursos.Foro.Hub.domain.topico.TopicoRepository;
import com.aluracursos.Foro.Hub.domain.usuario.Usuario;
import com.aluracursos.Foro.Hub.domain.usuario.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @PostMapping
    public ResponseEntity<DatosRespuestaTopico> registrarTopico(@RequestBody @Valid DatosRegistroTopico datosRegistroTopico,
                                                                UriComponentsBuilder uriComponentsBuilder){

        Curso curso = cursoRepository.findByNombre(datosRegistroTopico.curso())
                .orElseGet(() -> {
                    Curso nuevoCurso = new Curso();
                    nuevoCurso.setNombre(datosRegistroTopico.curso());
                    nuevoCurso.setCategoria("General"); // Valor por defecto para categoría
                    return cursoRepository.save(nuevoCurso);
                });

        Usuario autor = usuarioRepository.findByNombre(datosRegistroTopico.autor())
                .orElseGet(() -> {
                    Usuario nuevoAutor = new Usuario();
                    nuevoAutor.setNombre(datosRegistroTopico.autor());
                    nuevoAutor.setCorreoElectronico(datosRegistroTopico.autor().replaceAll(" ","") + "@example.com"); // Valor por defecto para correo
                    nuevoAutor.setContrasena("defaultPassword"); // Valor por defecto para contraseña
                    return usuarioRepository.save(nuevoAutor);
                });

        var topico = topicoRepository.save(new Topico(
                datosRegistroTopico,
                curso,
                autor));

        DatosRespuestaTopico datosRespuestaTopico =new DatosRespuestaTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getStatus().name(),
                topico.getAutor().getNombre(),
                topico.getCurso().getNombre(),
                topico.getRespuestas().stream()
                        .map(Respuesta::getMensaje)
                        .collect(Collectors.toList()));

        URI url = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaTopico);

    }
}
