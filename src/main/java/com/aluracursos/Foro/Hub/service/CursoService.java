package com.aluracursos.Foro.Hub.service;

import com.aluracursos.Foro.Hub.domain.curso.*;
import com.aluracursos.Foro.Hub.infra.exceptions.CursoNotFoundByIdException;
import com.aluracursos.Foro.Hub.infra.exceptions.UsuarioNotFoundByIdException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public DatosRespuestaCurso registrarCurso(DatosRegistroCurso datosRegistroCurso) {
        Curso curso = new Curso(datosRegistroCurso);

        cursoRepository.save(curso);
        return new DatosRespuestaCurso(
                curso.getId(),
                curso.getNombre(),
                curso.getCategoria());
    }

    public Curso actualizarCurso(Long id, DatosActualizarCurso datosActualizarCurso) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del curso debe ser un número positivo.");
        }

        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El curso con ID " + id + " no existe."));

        if (datosActualizarCurso.nombre() != null && !datosActualizarCurso.nombre().trim().isEmpty()) {
            String nombreLimpio = datosActualizarCurso.nombre().trim();
            curso.setNombre(nombreLimpio);
        }

        return cursoRepository.save(curso);
    }

    public Curso obtenerCurso(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new CursoNotFoundByIdException("No se encontró el Curso con ID " + id));
    }

    public Page<Curso> obtenerListadoCurso(Pageable paginacion) {
        Pageable paginacionConOrden = PageRequest.of(paginacion.getPageNumber(), 10, Sort.by(Sort.Order.asc("nombre")));
        return cursoRepository.findAll(paginacionConOrden);
    }

    @Transactional
    public void eliminarCurso(Long id) {
        var optionalCurso = cursoRepository.findById(id);
        if (optionalCurso.isEmpty()) {
            throw new CursoNotFoundByIdException("No se encontró el Usuario con ID " + id);
        }
        cursoRepository.deleteById(id);
    }
}
