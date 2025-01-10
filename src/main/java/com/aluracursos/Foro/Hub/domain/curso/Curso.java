package com.aluracursos.Foro.Hub.domain.curso;

import com.aluracursos.Foro.Hub.domain.topico.Topico;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "cursos")
@Entity(name = "Curso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Topico> topicos = new ArrayList<>();

    public Curso(DatosRegistroCurso datos) {
        this.nombre = datos.nombre();
        try {
            this.categoria = datos.categoria();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Categoría no válida: " + datos.categoria());
        }
    }

    public void actualizarDatos(DatosActualizarCurso datosActualizarCurso){
        if (datosActualizarCurso.nombre() != null
        && !datosActualizarCurso.nombre().trim().isEmpty()) {
            this.nombre = datosActualizarCurso.nombre();
        }
    }
}
