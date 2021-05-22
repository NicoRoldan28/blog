package com.API.blog.dto;

import com.API.blog.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    Integer id;
    String titulo;
    String imagen;
    String categoria;
    Date fechaDeCreacion;

    public static PostDto from(Post post){
        return PostDto.builder().id(post.getId()).titulo(post.getTitulo()).imagen(post.getImagen()).categoria(post.getCategoria()).fechaDeCreacion(post.getFechaDeCreacion()).build();
    }
}
