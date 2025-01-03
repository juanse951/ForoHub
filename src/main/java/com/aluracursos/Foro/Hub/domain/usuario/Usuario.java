package com.aluracursos.Foro.Hub.domain.usuario;

import com.aluracursos.Foro.Hub.domain.respuesta.Respuesta;
import com.aluracursos.Foro.Hub.domain.topico.Topico;
import com.aluracursos.Foro.Hub.domain.usuario.perfil.Perfil;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "usuarios")
@Entity(name = "Usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String correoElectronico;

    private String contrasena;

    @OneToOne
    @JoinColumn(name = "perfil_id")
    private Perfil perfil;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Topico> topicos = new ArrayList<>();

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Respuesta> respuestas = new ArrayList<>();
}
