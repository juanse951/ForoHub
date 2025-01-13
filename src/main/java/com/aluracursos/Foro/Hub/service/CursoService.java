package com.aluracursos.Foro.Hub.service;

import com.aluracursos.Foro.Hub.domain.curso.*;
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
            curso.setNombre(datosActualizarCurso.nombre());
        }

        return cursoRepository.save(curso);
    }
}
