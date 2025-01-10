package com.aluracursos.Foro.Hub.infra.exceptions;

public class TopicoAlreadyExistsException extends RuntimeException {

    public TopicoAlreadyExistsException(String message) {
        super(message);
    }
}