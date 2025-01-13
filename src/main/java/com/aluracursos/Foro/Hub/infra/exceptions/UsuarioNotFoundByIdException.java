package com.aluracursos.Foro.Hub.infra.exceptions;

public class UsuarioNotFoundByIdException extends RuntimeException {

    public UsuarioNotFoundByIdException(String message) {
        super(message);
    }
}
