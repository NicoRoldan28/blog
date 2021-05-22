package com.API.blog.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy  = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Integer id;

    private String titulo;

    private String contenido;

    private String imagen;

    private String categoria;

    private Date fechaDeCreacion;

    @OneToOne
    @JoinColumn(name = "id_user")
    private Usuario usuario;
}
