package com.aluracursos.Foro.Hub.domain.curso;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DatosRegistroCurso(

      @NotBlank(message = "{curso.obligatorio}")
      @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>?/|\\\\]*$", message = "{curso.error}")
      String nombre,

      Categoria categoria
) { }
