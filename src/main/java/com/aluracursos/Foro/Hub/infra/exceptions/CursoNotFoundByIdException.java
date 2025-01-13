package com.aluracursos.Foro.Hub.infra.exceptions;

public class CursoNotFoundByIdException extends RuntimeException {

    public CursoNotFoundByIdException(String message) {
        super(message);
    }
}
