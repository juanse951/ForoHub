package com.aluracursos.Foro.Hub.infra.errores;

import com.aluracursos.Foro.Hub.infra.exceptions.*;
import com.fasterxml.jackson.core.JsonParseException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.hibernate.NonUniqueResultException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class TratadorDeErrores {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> tratarError404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> tratarError400(MethodArgumentNotValidException e) {
        var errores = e.getFieldErrors().stream()
                .map(error -> error.getDefaultMessage()) // Solo extraemos el mensaje
                .toList();

        return ResponseEntity.badRequest().body(String.join(", ", errores));
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<String> tratarErrorDeIntegridad(SQLIntegrityConstraintViolationException e) {
        String mensajeOriginal = e.getMessage();
        String mensaje = analizarErrorDeIntegridad(mensajeOriginal);

        // Log del error
        System.err.println("Error de integridad en la base de datos: " + mensajeOriginal);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(mensaje);
    }

    @ExceptionHandler(TopicoAlreadyExistsException.class)
    public ResponseEntity<String> tratarErrorTopicoDuplicado(TopicoAlreadyExistsException e) {
        String mensaje = procesarMensajeError(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(mensaje);
    }

    @ExceptionHandler(TopicoNotFoundByIdException.class)
    public ResponseEntity<String> tratarErrorTopicoNoEncontrado(TopicoNotFoundByIdException e) {
        String mensaje = procesarMensajeError(e.getMessage());
        return ResponseEntity.badRequest().body(mensaje);
    }

    @ExceptionHandler(UsuarioNotFoundByIdException.class)
    public ResponseEntity<String> tratarErrorUsuarioNoEncontrado(UsuarioNotFoundByIdException e) {
        String mensaje = procesarMensajeError(e.getMessage());
        return ResponseEntity.badRequest().body(mensaje);
    }

    @ExceptionHandler(CursoNotFoundByIdException.class)
    public ResponseEntity<String> tratarErrorCursoNoEncontrado(CursoNotFoundByIdException e) {
        String mensaje = procesarMensajeError(e.getMessage());
        return ResponseEntity.badRequest().body(mensaje);
    }

    @ExceptionHandler(RespuestaNotFoundByIdException.class)
    public ResponseEntity<String> tratarErrorCursoNoEncontrado(RespuestaNotFoundByIdException e) {
        String mensaje = procesarMensajeError(e.getMessage());
        return ResponseEntity.badRequest().body(mensaje);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> manejarViolacionDeRestriccion(ConstraintViolationException e) {

        return ResponseEntity.badRequest().body(e.getMessage().replace("\"", ""));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> tratarErrorConflict(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  // Cambié esta línea
    }

    @ExceptionHandler(CursoNotFoundException.class)
    public ResponseEntity<String> tratarErrorNotFounCurso(CursoNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<String> tratarErrorNotFounUsuario(UsuarioNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<String> manejarErrorJsonParse(JsonParseException e) {
        String mensaje = "El formato del ID enviado no es válido. Verifique que los números no tengan ceros iniciales y que los datos estén correctamente formateados.";
        return ResponseEntity.badRequest().body(mensaje);
    }

    @ExceptionHandler(NonUniqueResultException.class)
    public ResponseEntity<String> manejarErrorNoUnico(NonUniqueResultException e) {

        String mensaje = "Parece que ya existen registros con los mismos datos EN LA BASE DE DATOS. Por favor, verifica la información ingresada y asegúrate de que sea única.";

        System.err.println("Error de resultado no único: " + e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(mensaje);
    }

    private String procesarMensajeError(String mensaje) {
        if (mensaje.contains("título") && mensaje.contains("mensaje")) {
            return "El título y el mensaje del tópico ya existen, en el ID: " + extraerId(mensaje);
        } else if (mensaje.contains("título")) {
            return "El título del tópico ya existe, en el ID:  " + extraerId(mensaje);
        } else if (mensaje.contains("mensaje")) {
            return "El mensaje del tópico ya existe, en el ID:  " + extraerId(mensaje);
        }
        return mensaje;
    }

    private String extraerId(String mensaje) {
        int startIndex = mensaje.indexOf("ID del tópico duplicado:") + "ID del tópico duplicado:".length();
        return mensaje.substring(startIndex).trim();
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

    public record DatosErrorValidacion(String campo, String error) {
        public DatosErrorValidacion(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }
}
