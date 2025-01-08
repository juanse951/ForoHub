package com.aluracursos.Foro.Hub.controller;

import com.aluracursos.Foro.Hub.domain.curso.Curso;
import com.aluracursos.Foro.Hub.domain.curso.CursoRepository;
import com.aluracursos.Foro.Hub.domain.curso.DatosRegistroCurso;
import com.aluracursos.Foro.Hub.domain.respuesta.DatosRegistroRespuesta;
import com.aluracursos.Foro.Hub.domain.respuesta.Respuesta;
import com.aluracursos.Foro.Hub.domain.respuesta.RespuestaRepository;
import com.aluracursos.Foro.Hub.domain.topico.*;
import com.aluracursos.Foro.Hub.domain.usuario.Usuario;
import com.aluracursos.Foro.Hub.domain.usuario.UsuarioRepository;
import com.aluracursos.Foro.Hub.domain.usuario.DatosRegistroUsuario;
import com.aluracursos.Foro.Hub.domain.usuario.perfil.DatosRegistroPerfil;
import com.aluracursos.Foro.Hub.domain.usuario.perfil.Perfil;
import com.aluracursos.Foro.Hub.domain.usuario.perfil.PerfilRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

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

    @PostMapping
    public ResponseEntity<DatosRespuestaTopico> registrarTopico(@RequestBody @Valid DatosRegistroTopico datosRegistroTopico,
                                                                UriComponentsBuilder uriComponentsBuilder){

        Curso curso = cursoRepository.findByNombre(datosRegistroTopico.curso())
                .orElseGet(() -> cursoRepository.save(DatosRegistroCurso.
                        registro(datosRegistroTopico.curso())));

        Usuario autor = usuarioRepository.findByNombre(datosRegistroTopico.autor())
                .orElseGet(() -> {
                    Usuario nuevoUsuario = DatosRegistroUsuario.registro(datosRegistroTopico.autor());

                    Perfil perfil = DatosRegistroPerfil.registro(null);
                    perfil = perfilRepository.save(perfil);
                    nuevoUsuario.setPerfil(perfil);

                    return usuarioRepository.save(nuevoUsuario);
                });

        var topico = topicoRepository.save(new Topico(
                datosRegistroTopico,
                curso,
                autor,
                new ArrayList<>())
        );

        Respuesta respuesta = DatosRegistroRespuesta.registro(null);
                    respuesta.setAutor(autor);
                    respuesta.setTopico(topico);

        topico.getRespuestas().add(respuesta);
        respuestaRepository.save(respuesta);

        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(topico);

        URI url = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaTopico);

    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoTopico>> listadoTopicos(@PageableDefault(size = 10) Pageable paginacion){
       Pageable paginacionConOrden = PageRequest.of(paginacion.getPageNumber(), 10, Sort.by(Sort.Order.desc("fechaCreacion")));
        return ResponseEntity.ok(topicoRepository.findAll(paginacionConOrden).map(DatosListadoTopico::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaTopico> retornarDatosTopico(@PathVariable Long id){
        Topico topico = topicoRepository.getReferenceById(id);
        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(topico);
        return ResponseEntity.ok(datosRespuestaTopico);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> actualizarTopico(@PathVariable Long id, @RequestBody @Valid DatosActualizarTopico datosActualizarTopico){

        var optionalTopico = topicoRepository.findById(id);
        if (optionalTopico.isEmpty()) {
            return ResponseEntity.notFound().build(); // Retorna 404 si no se encuentra el tópico
        }

        Topico topico = optionalTopico.get();

        topico.actualizarDatos(datosActualizarTopico);
        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(topico);
        return ResponseEntity.ok(datosRespuestaTopico);
    }
}
