package com.API.blog.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class Usuario {
    @Id
    @GeneratedValue(strategy  = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Integer id;

    private String email;

    private String password;

}
