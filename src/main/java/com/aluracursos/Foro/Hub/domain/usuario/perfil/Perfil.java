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

    @OneToMany(mappedBy = "perfil")
    private List<Usuario> usuarios = new ArrayList<>();

//    // Nueva propiedad para agregar una descripción del perfil
//    private String descripcion;
//
//    // Constructor adicional para inicializar sin descripción si no se necesita
//    public Perfil(String nombre) {
//        this.nombre = nombre;
//    }
}
