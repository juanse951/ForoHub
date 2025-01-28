package com.aluracursos.Foro.Hub.service;

import com.aluracursos.Foro.Hub.domain.curso.Curso;
import com.aluracursos.Foro.Hub.domain.topico.*;
import com.aluracursos.Foro.Hub.domain.usuario.Usuario;
import com.aluracursos.Foro.Hub.infra.exceptions.TopicoAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class TopicoServiceTest {

    @Autowired
    private TopicoService topicoService;

    @MockBean
    private TopicoRepository topicoRepository;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private CursoService cursoService;

    private Authentication authentication;

    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void crearTopico_deberiaCrearUnTopicoExitosamente() {
        DatosRegistroTopico datos = new DatosRegistroTopico("Titulo de prueba", "Mensaje de prueba", 1L);
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNombre("Curso de prueba");
        Usuario autor = new Usuario();
        autor.setId(1L);
        autor.setNombre("Autor de prueba");

        when(cursoService.obtenerCurso(1L)).thenReturn(curso);
        when(authentication.getName()).thenReturn("autor@test.com");
        when(usuarioService.obtenerUsuarioPorEmail("autor@test.com")).thenReturn(autor);
        when(topicoRepository.save(any(Topico.class))).thenAnswer(invocation -> {
            Topico topico = invocation.getArgument(0);
            topico.setId(1L);
            topico.setFechaCreacion(LocalDateTime.now());
            return topico;
        });

        DatosRespuestaTopico respuesta = topicoService.crearTopico(datos);

        assertNotNull(respuesta);
        assertEquals(1L, respuesta.id());
        assertEquals("Titulo de prueba", respuesta.titulo());
        assertEquals("Mensaje de prueba", respuesta.mensaje());
        assertNotNull(respuesta.fechaCreacion());
        assertEquals("Autor de prueba", respuesta.autor());
        assertEquals("Curso de prueba", respuesta.curso());

        verify(topicoRepository, times(1)).save(any(Topico.class));
    }

    @Test
    void crearTopico_deberiaLanzarExcepcionCuandoTituloYaExiste() {
        DatosRegistroTopico datos = new DatosRegistroTopico("Titulo duplicado", "Mensaje de prueba", 1L);

        when(topicoRepository.existsByTitulo("Titulo duplicado")).thenReturn(true);
        Curso cursoMock = mock(Curso.class);
        Usuario autorMock = mock(Usuario.class);
        when(topicoRepository.findByTitulo("Titulo duplicado"))
                .thenReturn(new Topico(1L, "Titulo duplicado", "Mensaje ejemplo",
                        LocalDateTime.now(), TopicoStatus.ACTIVO, autorMock, cursoMock, new ArrayList<>()));
        TopicoAlreadyExistsException exception = assertThrows(TopicoAlreadyExistsException.class, () -> {
            topicoService.crearTopico(datos);
        });

        assertEquals("El título del tópico ya existe. ID del tópico duplicado: 1", exception.getMessage());
        verify(topicoRepository, never()).save(any(Topico.class));
    }

    @Test
    void crearTopico_deberiaLanzarExcepcionCuandoMensajeYaExiste() {
        DatosRegistroTopico datos = new DatosRegistroTopico("Titulo único", "Mensaje duplicado", 1L);

        when(topicoRepository.existsByMensaje("Mensaje duplicado")).thenReturn(true);
        Curso cursoMock = mock(Curso.class);
        Usuario autorMock = mock(Usuario.class);
        when(topicoRepository.findByMensaje("Mensaje duplicado"))
                .thenReturn(new Topico(2L, "Titulo de prueba", "Mensaje duplicado",
                        LocalDateTime.now(), TopicoStatus.ACTIVO, autorMock, cursoMock, new ArrayList<>()));
        TopicoAlreadyExistsException exception = assertThrows(TopicoAlreadyExistsException.class, () -> {
            topicoService.crearTopico(datos);
        });

        assertEquals("El mensaje del tópico ya existe. ID del tópico duplicado: 2", exception.getMessage());
        verify(topicoRepository, never()).save(any(Topico.class));
    }

    @Test
    void crearTopico_deberiaLanzarExcepcionCuandoCursoNoExiste() {
        DatosRegistroTopico datos = new DatosRegistroTopico("Titulo de prueba", "Mensaje de prueba", 99L);

        when(cursoService.obtenerCurso(99L)).thenThrow(new RuntimeException("Curso no encontrado"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            topicoService.crearTopico(datos);
        });

        assertEquals("Curso no encontrado", exception.getMessage());
        verify(topicoRepository, never()).save(any(Topico.class));
    }

    @Test
    void crearTopico_deberiaLanzarExcepcionCuandoUsuarioNoExiste() {
        DatosRegistroTopico datos = new DatosRegistroTopico("Titulo de prueba", "Mensaje de prueba", 1L);
        Curso curso = new Curso();
        curso.setId(1L);

        when(cursoService.obtenerCurso(1L)).thenReturn(curso);
        when(authentication.getName()).thenReturn("usuario_inexistente@test.com");
        when(usuarioService.obtenerUsuarioPorEmail("usuario_inexistente@test.com")).thenThrow(new RuntimeException("Usuario no encontrado"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            topicoService.crearTopico(datos);
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(topicoRepository, never()).save(any(Topico.class));
    }

    @Test
    void obtenerTopico_deberiaRetornarTopicoCuandoExiste() {
        Topico topico = new Topico(1L, "Titulo existente", "Mensaje existente",
                LocalDateTime.now(), TopicoStatus.ACTIVO, new Usuario(), new Curso(), new ArrayList<>());

        when(topicoRepository.findById(1L)).thenReturn(Optional.of(topico));

        Topico resultado = topicoService.obtenerTopico(1L);

        assertNotNull(resultado);
        assertEquals("Titulo existente", resultado.getTitulo());
        assertEquals("Mensaje existente", resultado.getMensaje());
        verify(topicoRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerTopico_deberiaLanzarExcepcionCuandoNoExiste() {
        Long id = 99L;
        when(topicoRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            topicoService.obtenerTopico(id);
        });

        assertEquals("No se encontró el tópico con ID " + id, exception.getMessage());
        verify(topicoRepository, times(1)).findById(99L);
    }

    @Test
    void actualizarTopico_deberiaActualizarLosCamposCorrectos() {
        Long id = 1L;
        Topico topicoExistente = new Topico(id, "Titulo antiguo", "Mensaje antiguo",
                LocalDateTime.now(), TopicoStatus.ACTIVO, new Usuario(), new Curso(), new ArrayList<>());

        when(topicoRepository.findById(id)).thenReturn(Optional.of(topicoExistente));
        when(topicoRepository.save(any(Topico.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DatosActualizarTopico datosActualizar = new DatosActualizarTopico(
                "Titulo actualizado",
                "Mensaje actualizado",
                1L,
                1L);

        Topico actualizado = topicoService.actualizarTopico(id,datosActualizar);

        assertNotNull(actualizado);
        assertEquals("Titulo actualizado", actualizado.getTitulo());
        assertEquals("Mensaje actualizado", actualizado.getMensaje());
        verify(topicoRepository, times(1)).save(any(Topico.class));
    }

    @Test
    void actualizarTopico_deberiaLanzarExcepcionCuandoNoExiste() {
        Long id = 99L;
        DatosActualizarTopico datosActualizar = new DatosActualizarTopico(
                "Titulo actualizado",
                "Mensaje actualizado",
                1L,
                1L
        );


        when(topicoRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            topicoService.actualizarTopico(id,datosActualizar);
        });

        assertEquals("No se encontró el tópico con ID " + id, exception.getMessage());
        verify(topicoRepository, never()).save(any(Topico.class));
    }

    @Test
    void eliminarTopico_deberiaEliminarCuandoExiste() {
        Topico topicoExistente = new Topico(1L, "Titulo de prueba", "Mensaje de prueba",
                LocalDateTime.now(), TopicoStatus.ACTIVO, new Usuario(), new Curso(), new ArrayList<>());

        when(topicoRepository.findById(1L)).thenReturn(Optional.of(topicoExistente));
        doNothing().when(topicoRepository).deleteById(1L);

        topicoService.eliminarTopico(1L);

        verify(topicoRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarTopico_deberiaLanzarExcepcionCuandoNoExiste() {
        Long id = 99L;
        when(topicoRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            topicoService.eliminarTopico(id);
        });

        assertEquals("No se encontró el tópico con ID " + id, exception.getMessage());
        verify(topicoRepository, never()).deleteById(99L);
    }

    @Test
    void obtenerListadoTopicos_deberiaRetornarUnaPaginaDeTopicosCuandoExistenTopicos() {
        Pageable paginacion = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("fechaCreacion")));
        Page<Topico> paginaDeTopicos = mock(Page.class);

        when(topicoRepository.findAll(paginacion)).thenReturn(paginaDeTopicos);

        Page<Topico> resultado = topicoService.obtenerListadoTopicos(paginacion);

        assertNotNull(resultado);
        assertEquals(paginaDeTopicos, resultado);
        verify(topicoRepository, times(1)).findAll(paginacion);
    }

    @Test
    void obtenerListadoTopicos_deberiaRetornarPaginaVaciaCuandoNoExistenTopicos() {
        Pageable paginacion = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("fechaCreacion")));
        Page<Topico> paginaDeTopicos = Page.empty();

        when(topicoRepository.findAll(paginacion)).thenReturn(paginaDeTopicos);

        Page<Topico> resultado = topicoService.obtenerListadoTopicos(paginacion);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(topicoRepository, times(1)).findAll(paginacion);
    }
}