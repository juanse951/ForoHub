package com.aluracursos.Foro.Hub.infra.exceptions;

public class TopicoNotFoundByIdException extends RuntimeException{

    public TopicoNotFoundByIdException(String message) {
        super(message);
    }
}
