package com.aluracursos.Foro.Hub.service;

import com.aluracursos.Foro.Hub.domain.respuesta.*;
import com.aluracursos.Foro.Hub.domain.topico.Topico;
import com.aluracursos.Foro.Hub.domain.usuario.Usuario;
import com.aluracursos.Foro.Hub.infra.exceptions.RespuestaNotFoundByIdException;
import org.junit.jupiter.api.Test;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class RespuestaServiceTest {

    @Autowired
    private RespuestaService respuestaService;

    @MockBean
    private RespuestaRepository respuestaRepository;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private TopicoService topicoService;

    @Test
    void agregarRespuesta_DatosValidos_RegistraRespuestaExitosamente() {
        Long topicoId = 1L;
        DatosRegistroRespuesta datosRegistroRespuesta = new DatosRegistroRespuesta("Mensaje de prueba");

        Topico topicoMock = new Topico();
        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setCorreoElectronico("test@correo.com");

        Authentication autenticacion = mock(Authentication.class);
        SecurityContext contextoSeguridad = mock(SecurityContext.class);
        when(contextoSeguridad.getAuthentication()).thenReturn(autenticacion);
        when(autenticacion.getName()).thenReturn("test@correo.com");
        SecurityContextHolder.setContext(contextoSeguridad);

        when(topicoService.obtenerTopico(topicoId)).thenReturn(topicoMock);
        when(usuarioService.obtenerUsuarioPorEmail("test@correo.com")).thenReturn(usuarioMock);

        Respuesta respuestaEsperada = new Respuesta();
        respuestaEsperada.setId(1L);
        respuestaEsperada.setTopico(topicoMock);
        respuestaEsperada.setFechaCreacion(LocalDateTime.now());
        respuestaEsperada.setAutor(usuarioMock);
        respuestaEsperada.setMensaje(datosRegistroRespuesta.mensaje().trim());
        respuestaEsperada.setSolucion(RespuestaStatus.PENDIENTE);

        when(respuestaRepository.save(any(Respuesta.class))).thenReturn(respuestaEsperada);

        Respuesta respuestaObtenida = respuestaService.agregarRespuesta(topicoId, datosRegistroRespuesta);

        assertNotNull(respuestaObtenida);
        assertEquals(respuestaEsperada.getId(), respuestaObtenida.getId());
        assertEquals(respuestaEsperada.getMensaje(), respuestaObtenida.getMensaje());
        assertEquals(respuestaEsperada.getAutor(), respuestaObtenida.getAutor());
        assertEquals(respuestaEsperada.getTopico(), respuestaObtenida.getTopico());
        assertEquals(respuestaEsperada.getSolucion(), respuestaObtenida.getSolucion());

        verify(topicoService).obtenerTopico(topicoId);
        verify(usuarioService).obtenerUsuarioPorEmail("test@correo.com");
        verify(respuestaRepository).save(any(Respuesta.class));
    }

    @Test
    void agregarRespuesta_MensajeNulo_LanzaExcepcion() {
        Long topicoId = 1L;
        DatosRegistroRespuesta datosRegistroRespuesta = new DatosRegistroRespuesta(null);

        Exception excepcion = assertThrows(IllegalArgumentException.class,
                () -> respuestaService.agregarRespuesta(topicoId, datosRegistroRespuesta));

        assertNotNull(excepcion);
        assertEquals("El mensaje no puede estar vacío.", excepcion.getMessage());
    }

    @Test
    void agregarRespuesta_MensajeVacio_LanzaExcepcion() {
        Long topicoId = 1L;
        DatosRegistroRespuesta datosRegistroRespuesta = new DatosRegistroRespuesta("");

        Topico topicoMock = new Topico();
        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setCorreoElectronico("test@correo.com");

        Authentication autenticacion = mock(Authentication.class);
        SecurityContext contextoSeguridad = mock(SecurityContext.class);
        when(contextoSeguridad.getAuthentication()).thenReturn(autenticacion);
        when(autenticacion.getName()).thenReturn("test@correo.com");
        SecurityContextHolder.setContext(contextoSeguridad);

        when(topicoService.obtenerTopico(topicoId)).thenReturn(topicoMock);
        when(usuarioService.obtenerUsuarioPorEmail("test@correo.com")).thenReturn(usuarioMock);

        Exception excepcion = assertThrows(IllegalArgumentException.class,
                () -> respuestaService.agregarRespuesta(topicoId, datosRegistroRespuesta));

        assertNotNull(excepcion);
    }

    @Test
    void agregarRespuesta_TopicoNoExiste_LanzaExcepcion() {
        Long topicoId = 99L;
        DatosRegistroRespuesta datosRegistroRespuesta = new DatosRegistroRespuesta("Mensaje de prueba");
    }


    @Test
    void actualizarRespuesta_DatosValidos_ActualizaRespuestaExitosamente() {
        Long respuestaId = 1L;
        DatosActualizarRespuesta datosActualizarRespuesta = new DatosActualizarRespuesta("Mensaje actualizado", RespuestaStatus.SOLUCIONADO);

        Respuesta respuestaMock = new Respuesta();
        respuestaMock.setId(respuestaId);
        respuestaMock.setMensaje("Mensaje original");
        respuestaMock.setSolucion(RespuestaStatus.PENDIENTE);

        when(respuestaRepository.findById(respuestaId)).thenReturn(java.util.Optional.of(respuestaMock));
        when(respuestaRepository.save(any(Respuesta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Respuesta respuestaActualizada = respuestaService.actualizarRespuesta(respuestaId, datosActualizarRespuesta);

        assertNotNull(respuestaActualizada);
        assertEquals("Mensaje actualizado", respuestaActualizada.getMensaje());
        assertEquals(RespuestaStatus.SOLUCIONADO, respuestaActualizada.getSolucion());
        verify(respuestaRepository).findById(respuestaId);
        verify(respuestaRepository).save(any(Respuesta.class));
    }

    @Test
    void actualizarRespuesta_RespuestaNoExiste_LanzaExcepcion() {
        Long respuestaId = 99L;
        DatosActualizarRespuesta datosActualizarRespuesta = new DatosActualizarRespuesta("Mensaje actualizado", RespuestaStatus.SOLUCIONADO);

        when(respuestaRepository.findById(respuestaId)).thenReturn(java.util.Optional.empty());

        Exception excepcion = assertThrows(IllegalArgumentException.class,
                () -> respuestaService.actualizarRespuesta(respuestaId, datosActualizarRespuesta));

        assertNotNull(excepcion);
        verify(respuestaRepository).findById(respuestaId);
        verify(respuestaRepository, never()).save(any(Respuesta.class));
    }

    @Test
    void actualizarRespuesta_IdInvalido_LanzaExcepcion() {
        Long respuestaId = -1L;
        DatosActualizarRespuesta datosActualizarRespuesta = new DatosActualizarRespuesta("Mensaje actualizado", RespuestaStatus.SOLUCIONADO);

        Exception excepcion = assertThrows(IllegalArgumentException.class,
                () -> respuestaService.actualizarRespuesta(respuestaId, datosActualizarRespuesta));

        assertNotNull(excepcion);
        verify(respuestaRepository, never()).findById(respuestaId);
    }

    @Test
    void actualizarRespuesta_DatosNulosNoModificaCampos() {
        Long respuestaId = 1L;
        DatosActualizarRespuesta datosActualizarRespuesta = new DatosActualizarRespuesta(null, null);

        Respuesta respuestaMock = new Respuesta();
        respuestaMock.setId(respuestaId);
        respuestaMock.setMensaje("Mensaje original");
        respuestaMock.setSolucion(RespuestaStatus.PENDIENTE);

        when(respuestaRepository.findById(respuestaId)).thenReturn(java.util.Optional.of(respuestaMock));
        when(respuestaRepository.save(any(Respuesta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Respuesta respuestaSinCambios = respuestaService.actualizarRespuesta(respuestaId, datosActualizarRespuesta);

        assertNotNull(respuestaSinCambios);
        assertEquals("Mensaje original", respuestaSinCambios.getMensaje());
        assertEquals(RespuestaStatus.PENDIENTE, respuestaSinCambios.getSolucion());
        verify(respuestaRepository).findById(respuestaId);
        verify(respuestaRepository).save(any(Respuesta.class));
    }

    @Test
    void obtenerListadoRespuesta_PaginacionCorrecta_DevuelvePaginaOrdenada() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("fechaCreacion")));
        Page<Respuesta> respuestaPaginadaMock = mock(Page.class);

        when(respuestaRepository.findAll(pageable)).thenReturn(respuestaPaginadaMock);

        Page<Respuesta> resultado = respuestaService.obtenerListadoRespuesta(pageable);

        assertNotNull(resultado);
        verify(respuestaRepository).findAll(pageable);
    }

    @Test
    void obtenerListadoRespuesta_PaginaFueraDeRango_DevuelvePaginaVacia() {
        Pageable pageable = PageRequest.of(100, 10, Sort.by(Sort.Order.asc("fechaCreacion")));
        Page<Respuesta> respuestaPaginadaMock = Page.empty(pageable);

        when(respuestaRepository.findAll(pageable)).thenReturn(respuestaPaginadaMock);

        Page<Respuesta> resultado = respuestaService.obtenerListadoRespuesta(pageable);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(respuestaRepository).findAll(pageable);
    }

    @Test
    void obtenerListadoRespuesta_ListaVacia_DevuelvePaginaVacia() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("fechaCreacion")));
        Page<Respuesta> respuestaPaginadaMock = Page.empty(pageable);

        when(respuestaRepository.findAll(pageable)).thenReturn(respuestaPaginadaMock);

        Page<Respuesta> resultado = respuestaService.obtenerListadoRespuesta(pageable);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(respuestaRepository).findAll(pageable);
    }

    @Test
    void obtenerRespuesta_IdValido_DevuelveRespuesta() {
        Long respuestaId = 1L;

        Respuesta respuestaMock = new Respuesta();
        respuestaMock.setId(respuestaId);
        respuestaMock.setMensaje("Mensaje de prueba");

        when(respuestaRepository.findById(respuestaId)).thenReturn(Optional.of(respuestaMock));

        Respuesta respuestaObtenida = respuestaService.obtenerRespuesta(respuestaId);

        assertNotNull(respuestaObtenida);
        assertEquals(respuestaMock.getId(), respuestaObtenida.getId());
        assertEquals(respuestaMock.getMensaje(), respuestaObtenida.getMensaje());
        verify(respuestaRepository).findById(respuestaId);
    }

    @Test
    void obtenerRespuesta_IdInvalido_RetornaExcepcion() {
        Long respuestaId = 99L;

        when(respuestaRepository.findById(respuestaId)).thenReturn(Optional.empty());

        Exception excepcion = assertThrows(RespuestaNotFoundByIdException.class,
                () -> respuestaService.obtenerRespuesta(respuestaId));

        assertNotNull(excepcion);
        verify(respuestaRepository).findById(respuestaId);
    }

    @Test
    void eliminarRespuesta_IdValido_EliminaRespuestaCorrectamente() {
        Long respuestaId = 1L;

        Respuesta respuestaMock = new Respuesta();
        respuestaMock.setId(respuestaId);

        when(respuestaRepository.findById(respuestaId)).thenReturn(Optional.of(respuestaMock));
        doNothing().when(respuestaRepository).deleteById(respuestaId);

        assertDoesNotThrow(() -> respuestaService.eliminarRespuesta(respuestaId));
        verify(respuestaRepository).findById(respuestaId);
        verify(respuestaRepository).deleteById(respuestaId);
    }

    @Test
    void eliminarRespuesta_IdInvalido_ArrojaExcepcion() {
        Long respuestaId = 99L;

        when(respuestaRepository.findById(respuestaId)).thenReturn(Optional.empty());

        Exception excepcion = assertThrows(RespuestaNotFoundByIdException.class,
                () -> respuestaService.eliminarRespuesta(respuestaId));

        assertNotNull(excepcion);
        verify(respuestaRepository).findById(respuestaId);
        verify(respuestaRepository, never()).deleteById(anyLong());
    }
}