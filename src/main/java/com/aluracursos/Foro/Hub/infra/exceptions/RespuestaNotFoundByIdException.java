package com.aluracursos.Foro.Hub.infra.exceptions;

public class RespuestaNotFoundByIdException extends RuntimeException {

    public RespuestaNotFoundByIdException(String message) {
        super(message);
    }
}
