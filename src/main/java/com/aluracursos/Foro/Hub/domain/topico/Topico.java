package com.aluracursos.Foro.Hub.domain.topico;

import com.aluracursos.Foro.Hub.domain.curso.Curso;
import com.aluracursos.Foro.Hub.domain.respuesta.Respuesta;
import com.aluracursos.Foro.Hub.domain.usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Table(name = "topicos")
@Entity(name = "Topico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String mensaje;

    @Column(name = "fecha_creacion")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    private TopicoStatus status;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Usuario autor;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @OneToMany(mappedBy = "topico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Respuesta> respuestas = new ArrayList<>();

    public Topico(DatosRegistroTopico datosRegistroTopico, Curso curso, Usuario autor, List<Respuesta> respuestas) {
        this.titulo = datosRegistroTopico.titulo();
        this.mensaje = datosRegistroTopico.mensaje();
        this.curso = curso;
        this.autor = autor;
        this.fechaCreacion = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        this.status = TopicoStatus.ACTIVO;
        this.respuestas = respuestas != null ? respuestas : new ArrayList<>();
    }

    public void actualizarDatos(DatosActualizarTopico datosActualizarTopico) {
        if(datosActualizarTopico.titulo() != null && !datosActualizarTopico.titulo().trim().isEmpty()){
            this.titulo = datosActualizarTopico.titulo();
        }
        if(datosActualizarTopico.mensaje() != null && !datosActualizarTopico.mensaje().trim().isEmpty()){
            this.mensaje = datosActualizarTopico.mensaje();
        }
        if (datosActualizarTopico.autor() != null
                && datosActualizarTopico.autor().nombre() != null
                && !datosActualizarTopico.autor().nombre().trim().isEmpty()) {
            this.autor.actualizarDatos(datosActualizarTopico.autor());
        }
        if (datosActualizarTopico.curso() != null
                && datosActualizarTopico.curso().nombre() != null
                && !datosActualizarTopico.curso().nombre().trim().isEmpty()) {
            this.curso.actualizarDatos(datosActualizarTopico.curso());
        }
    }
}
