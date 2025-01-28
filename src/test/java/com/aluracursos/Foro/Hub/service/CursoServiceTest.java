package com.aluracursos.Foro.Hub.service;

import com.aluracursos.Foro.Hub.domain.curso.*;
import com.aluracursos.Foro.Hub.infra.exceptions.CursoNotFoundByIdException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class CursoServiceTest {

    @Autowired
    private CursoService cursoService;

    @MockBean
    private CursoRepository cursoRepository;

    @Test
    void registrarCurso_CursoValido_DeberiaRegistrarCurso() {
        Long id = 1L;
        DatosRegistroCurso datosRegistro = new DatosRegistroCurso("Curso de Java", Categoria.PROGRAMACION);
        Curso curso = new Curso(datosRegistro);
        curso.setId(id);

        when(cursoRepository.findByNombre(eq("Curso de Java"))).thenReturn(java.util.Optional.empty());
        when(cursoRepository.save(any(Curso.class))).thenAnswer(invocation -> {
            Curso cursoGuardado = invocation.getArgument(0);
            cursoGuardado.setId(id);
            return cursoGuardado;
        });

        DatosRespuestaCurso respuestaCurso = cursoService.registrarCurso(datosRegistro);

        assertNotNull(respuestaCurso);
        assertNotNull(respuestaCurso.id());
        assertEquals(id, respuestaCurso.id());
        assertEquals("Curso de Java", respuestaCurso.nombre());
        assertEquals(Categoria.PROGRAMACION, respuestaCurso.categoria());
        Mockito.verify(cursoRepository).findByNombre(eq("Curso de Java"));
        Mockito.verify(cursoRepository).save(any(Curso.class));
    }

    @Test
    void registrarCurso_CursoExistente_DeberiaLanzarExcepcion() {
        Long id = 1L;
        DatosRegistroCurso datosRegistro = new DatosRegistroCurso("Curso de Java", Categoria.PROGRAMACION);
        Curso cursoExistente = new Curso(datosRegistro);
        cursoExistente.setId(id);

        when(cursoRepository.findByNombre(eq("Curso de Java"))).thenReturn(java.util.Optional.of(cursoExistente));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cursoService.registrarCurso(datosRegistro);
        });

        assertEquals("El nombre del curso ya está registrado, buscalo en el foro :) bajo el ID: " + id, exception.getMessage());
        Mockito.verify(cursoRepository).findByNombre(eq("Curso de Java"));
        Mockito.verify(cursoRepository, Mockito.never()).save(any(Curso.class));
    }

    @Test
    void actualizarCurso_CursoValido_DeberiaActualizarCurso() {
        Long id = 1L;
        DatosActualizarCurso datosActualizar = new DatosActualizarCurso("Nuevo Nombre", Categoria.GENERAL);
        Curso cursoExistente = new Curso();
        cursoExistente.setId(id);
        cursoExistente.setNombre("Curso Original");
        cursoExistente.setCategoria(Categoria.PROGRAMACION);

        when(cursoRepository.findById(eq(id))).thenReturn(java.util.Optional.of(cursoExistente));
        when(cursoRepository.findByNombre(eq("Nuevo Nombre"))).thenReturn(java.util.Optional.empty());
        when(cursoRepository.save(any(Curso.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Curso cursoActualizado = cursoService.actualizarCurso(id, datosActualizar);

        assertNotNull(cursoActualizado);
        assertEquals("Nuevo Nombre", cursoActualizado.getNombre());
        assertEquals(Categoria.GENERAL, cursoActualizado.getCategoria());
        Mockito.verify(cursoRepository).findById(eq(id));
        Mockito.verify(cursoRepository).findByNombre(eq("Nuevo Nombre"));
        Mockito.verify(cursoRepository).save(any(Curso.class));
    }

    @Test
    void actualizarCurso_CursoInexistente_DeberiaLanzarExcepcion() {
        Long id = 1L;
        DatosActualizarCurso datosActualizar = new DatosActualizarCurso("Nuevo Nombre", Categoria.GENERAL);

        when(cursoRepository.findById(eq(id))).thenReturn(java.util.Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cursoService.actualizarCurso(id, datosActualizar);
        });

        assertEquals("El curso con ID " + id + " no existe.", exception.getMessage());
        Mockito.verify(cursoRepository).findById(eq(id));
        Mockito.verify(cursoRepository, Mockito.never()).findByNombre(any());
        Mockito.verify(cursoRepository, Mockito.never()).save(any());
    }

    @Test
    void actualizarCurso_NombreDuplicado_DeberiaLanzarExcepcion() {
        Long id = 1L;
        DatosActualizarCurso datosActualizar = new DatosActualizarCurso("Nombre Existente", Categoria.GENERAL);
        Curso cursoExistente = new Curso();
        cursoExistente.setId(id);
        cursoExistente.setNombre("Curso Original");
        cursoExistente.setCategoria(Categoria.PROGRAMACION);

        Curso cursoExistenteConNombre = new Curso();
        cursoExistenteConNombre.setId(2L);

        when(cursoRepository.findById(eq(id))).thenReturn(java.util.Optional.of(cursoExistente));
        when(cursoRepository.findByNombre(eq("Nombre Existente"))).thenReturn(java.util.Optional.of(cursoExistenteConNombre));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cursoService.actualizarCurso(id, datosActualizar);
        });

        assertEquals("El nombre del curso ya está registrado, buscalo en el foro :) bajo el ID: " + cursoExistenteConNombre.getId(), exception.getMessage());
        Mockito.verify(cursoRepository).findById(eq(id));
        Mockito.verify(cursoRepository).findByNombre(eq("Nombre Existente"));
        Mockito.verify(cursoRepository, Mockito.never()).save(any());
    }

    @Test
    void obtenerCurso_DeberiaRetornarCursoCuandoExiste() {
        Long id = 1L;
        Curso curso = new Curso();
        curso.setId(id);
        curso.setNombre("Curso de prueba");
        curso.setCategoria(Categoria.PROGRAMACION);
    }

    @Test
    void obtenerCurso_DeberiaLanzarExcepcionCuandoNoExiste() {
        Long id = 99L;
        when(cursoRepository.findById(id)).thenReturn(java.util.Optional.empty());
    }

    @Test
    void obtenerListadoCurso_DeberiaRetornarCursosPaginados() {
        Pageable paginacion = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("nombre")));
        Curso curso1 = new Curso();
        curso1.setId(1L);
        curso1.setNombre("Curso A");
        curso1.setCategoria(Categoria.PROGRAMACION);

        Curso curso2 = new Curso();
        curso2.setId(2L);
        curso2.setNombre("Curso B");
        curso2.setCategoria(Categoria.GENERAL);

        Page<Curso> pagina = new org.springframework.data.domain.PageImpl<>(List.of(curso1, curso2), paginacion, 2);

        when(cursoRepository.findAll(any(Pageable.class))).thenReturn(pagina);

        Page<Curso> resultado = cursoService.obtenerListadoCurso(paginacion);

        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalElements());
        assertEquals("Curso A", resultado.getContent().get(0).getNombre());
        assertEquals("Curso B", resultado.getContent().get(1).getNombre());
        Mockito.verify(cursoRepository).findAll(any(Pageable.class));
    }

    @Test
    void obtenerListadoCurso_DeberiaRetornarPaginaVaciaCuandoNoHayCursos() {
        Pageable paginacion = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("nombre")));

        Page<Curso> paginaVacia = new org.springframework.data.domain.PageImpl<>(List.of(), paginacion, 0);

        when(cursoRepository.findAll(any(Pageable.class))).thenReturn(paginaVacia);

        Page<Curso> resultado = cursoService.obtenerListadoCurso(paginacion);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.getTotalElements());
        Mockito.verify(cursoRepository).findAll(any(Pageable.class));
    }

    @Test
    void eliminarCurso_CursoExistente_DeberiaEliminarCurso() {
        Long id = 1L;
        Curso curso = new Curso();
        curso.setId(id);

        when(cursoRepository.findById(eq(id))).thenReturn(java.util.Optional.of(curso));

        assertDoesNotThrow(() -> cursoService.eliminarCurso(id));

        Mockito.verify(cursoRepository).findById(eq(id));
        Mockito.verify(cursoRepository).deleteById(eq(id));
    }

    @Test
    void eliminarCurso_CursoInexistente_DeberiaLanzarExcepcion() {
        Long id = 99L;

        when(cursoRepository.findById(eq(id))).thenReturn(java.util.Optional.empty());

        CursoNotFoundByIdException exception = assertThrows(CursoNotFoundByIdException.class, () -> cursoService.eliminarCurso(id));

        assertEquals("No se encontró el Usuario con ID " + id, exception.getMessage());
        Mockito.verify(cursoRepository).findById(eq(id));
        Mockito.verify(cursoRepository, Mockito.never()).deleteById(eq(id));
    }
}