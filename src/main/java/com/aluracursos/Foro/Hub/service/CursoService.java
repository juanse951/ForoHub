package com.aluracursos.Foro.Hub.service;

import com.aluracursos.Foro.Hub.domain.curso.*;
import com.aluracursos.Foro.Hub.infra.exceptions.CursoNotFoundByIdException;
import org.springframework.beans.factory.annotation.Autowired;
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


}
