package com.aluracursos.Foro.Hub.service;

import com.aluracursos.Foro.Hub.domain.curso.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public Curso crearCurso(DatosRegistroCurso datos) {
        Categoria categoria = Categoria.GENERAL;

        Curso curso = new Curso(null, datos.nombre(), categoria, new ArrayList<>());
        return cursoRepository.save(curso);
    }

    public Curso actualizarCurso(Long id, DatosActualizarCurso datosActualizarCurso) {
        Curso curso = cursoRepository.getReferenceById(id);

        if (datosActualizarCurso.nombre() != null && !datosActualizarCurso.nombre().trim().isEmpty()) {
            curso.setNombre(datosActualizarCurso.nombre());
        }

        return cursoRepository.save(curso);
    }
}
