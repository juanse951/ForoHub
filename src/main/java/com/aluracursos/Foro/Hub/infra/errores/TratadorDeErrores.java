package com.aluracursos.Foro.Hub.infra.errores;

import com.aluracursos.Foro.Hub.infra.exceptions.TopicoNotFoundByIdException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestControllerAdvice
public class TratadorDeErrores {


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarError404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarError400(MethodArgumentNotValidException e) {
        var errores = e.getFieldErrors().stream()
                .map(DatosErrorValidacion::new).toList();

        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity tratarErrorDeIntegridad(SQLIntegrityConstraintViolationException e) {
        String mensajeOriginal = e.getMessage();
        String detalle = analizarErrorDeIntegridad(mensajeOriginal);

        // Log del error
        System.err.println("Error de integridad en la base de datos: " + mensajeOriginal);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(detalle);
    }

    private String analizarErrorDeIntegridad(String mensaje) {
        if (mensaje.contains("Duplicate entry")) {
            return procesarEntradaDuplicada(mensaje);
        } else if (mensaje.contains("FOREIGN KEY")) {
            return "Operación no permitida: existen datos relacionados en otra tabla, o el dato referenciado no existe.";
        }
        return "Error de integridad en la base de datos. Detalle técnico: " + mensaje;
    }

    private String procesarEntradaDuplicada(String mensaje) {
        try {
            int startEntry = mensaje.indexOf("Duplicate entry '") + 17;
            int endEntry = mensaje.indexOf("'", startEntry);
            String valorDuplicado = mensaje.substring(startEntry, endEntry);

            int startKey = mensaje.indexOf("for key '") + 9;
            int endKey = mensaje.indexOf("'", startKey);
            String claveDuplicada = mensaje.substring(startKey, endKey);

            return String.format(
                    "Ya existe un registro con el valor duplicado: '%s' en el campo o índice '%s'.",
                    valorDuplicado, claveDuplicada);
        } catch (Exception e) {
            return "Error al procesar el mensaje de entrada duplicada.";
        }
    }

    @ExceptionHandler(TopicoNotFoundByIdException.class)
    public ResponseEntity tratarErrorTopicoNoEncontrado(TopicoNotFoundByIdException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    public record DatosErrorValidacion(String campo, String error) {
        public DatosErrorValidacion(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }
}