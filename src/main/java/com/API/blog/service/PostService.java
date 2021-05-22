package com.API.blog.service;

import com.API.blog.domain.Post;
import com.API.blog.exceptions.PostExistsException;
import com.API.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository){
        this.postRepository=postRepository;
    }

    public Post newPost(Post post)throws PostExistsException {
        if ((!postRepository.existsById(post.getId()))){
            return postRepository.save(post);
        } else {
        throw new PostExistsException();
    }
    }

    public void deletePost(Integer id){
        Post post= findById(id);
        postRepository.delete(post);
    }


    public Page<Post> allPost(Pageable pageable){
        return this.postRepository.findAll(pageable);
    }

    public List<Post> filterPostByTitulo(String titulo){
        return postRepository.findByTitulo(titulo);
    }

    public List<Post> filterPostByCategoria(String categoria){
        return postRepository.findByCategoria(categoria);
    }

    public Post findById(Integer id){
        return postRepository.findById(id)
                .orElseThrow(()->new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public void updatePost(Integer id,Post postDetails){
        Post post=findById(id);

        post.setCategoria(postDetails.getCategoria());
        post.setFechaDeCreacion(postDetails.getFechaDeCreacion());
        post.setImagen(postDetails.getImagen());
        post.setContenido(postDetails.getContenido());
        post.setTitulo(postDetails.getTitulo());

        postRepository.save(post);
    }



}
