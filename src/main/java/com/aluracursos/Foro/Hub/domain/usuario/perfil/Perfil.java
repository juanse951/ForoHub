package com.aluracursos.Foro.Hub.domain.usuario.perfil;

import com.aluracursos.Foro.Hub.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "perfiles")
@Entity(name = "Perfil")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @ManyToMany(mappedBy = "perfil")
    private List<Usuario> usuario = new ArrayList<>();

    public Perfil(DatosRegistroPerfil datos){
        this.nombre = datos.nombre();
    }

}
