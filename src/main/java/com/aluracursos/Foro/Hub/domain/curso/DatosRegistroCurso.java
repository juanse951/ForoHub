package com.aluracursos.Foro.Hub.domain.curso;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DatosRegistroCurso(

      @NotBlank(message = "{curso.obligatorio}")
      @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>?/|\\\\]*$", message = "{curso.error}")
      @Schema(example = "__")
      String nombre,

      @NotNull(message = "{categoria.obligatorio}")
      @Schema(example = "Elige una categoría de /categorias")
      Categoria categoria
) { }
