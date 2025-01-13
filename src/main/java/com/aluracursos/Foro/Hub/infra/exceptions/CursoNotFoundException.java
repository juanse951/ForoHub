package com.aluracursos.Foro.Hub.infra.exceptions;

public class CursoNotFoundException extends RuntimeException {
    public CursoNotFoundException(String message) {
        super(message);
    }
}
