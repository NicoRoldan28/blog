package com.API.blog.controller;

import com.API.blog.domain.Post;
import com.API.blog.exceptions.PostExistsException;
import com.API.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/")
public class PostController {
    public final PostService postService;

    @Autowired
    public PostController(PostService postService){
        this.postService=postService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity newPost(@RequestBody Post post) throws PostExistsException {
        Post newPost = postService.newPost(post);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newPost.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "posts")
    public ResponseEntity<List<Post>> allPost(Pageable pageable){
        Page p= postService.allPost(pageable);
        return ResponseEntity.
                status(HttpStatus.OK).
                header("X-Total-Count", Long.toString(p.getTotalElements())).
                header("X-Total-Pages", Long.toString(p.getTotalPages())).
                body(p.getContent());
    }
    @GetMapping(value = "post/titulo",params = {"titulo"})
    public ResponseEntity<List<Post>> postsByTitulo(@RequestParam("titulo")String titulo){
        List<Post> filteredList = postService.filterPostByTitulo(titulo);
        return response(filteredList);
    }

    @GetMapping(value = "post/categoria",params = {"categoria"})
    public ResponseEntity<List<Post>> postsByCategoria(@RequestParam("categoria")String categoria){
        List<Post> filteredList = postService.filterPostByCategoria(categoria);
        return response(filteredList);
    }

    @GetMapping(value = "id",params = {"id"})
    public ResponseEntity<Post> postById(@RequestParam("id")Integer id){
        Post post=postService.findById(id);
        return ResponseEntity.ok(post);
    }

    @PatchMapping("/{id}/jugador/{idJugador}")
    public ResponseEntity<Post> updatePost(@RequestParam("id")Integer id, @RequestBody Post postDetails){
        postService.updatePost(id,postDetails);
        return ResponseEntity.ok(postDetails);
    }

    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable Integer id) {
        postService.deletePost(id);
    }

    private ResponseEntity response(List list) {
        return ResponseEntity.status(list.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK).body(list);
    }

}
