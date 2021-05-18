package com.API.blog.controller;

import com.API.blog.domain.Usuario;
import com.API.blog.service.UsuarioService;
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
@RequestMapping(value = "/users")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    private UsuarioController(UsuarioService usuarioService){
        this.usuarioService=usuarioService;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity newClient(@RequestBody Usuario user){
        Usuario newUser = usuarioService.newUsuario(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Usuario>> allUsuario(Pageable pageable) {
        Page p = usuarioService.allUsuarios(pageable);
        return ResponseEntity.
                status(HttpStatus.OK).
                header("X-Total-Count", Long.toString(p.getTotalElements())).
                header("X-Total-Pages", Long.toString(p.getTotalPages())).
                body(p.getContent());
    }

    @GetMapping(value = "{id}", produces = "application/json")
    public ResponseEntity<Usuario> userByCode(@PathVariable("id") Integer id){
        Usuario user = usuarioService.getUsuarioById(id);
        return ResponseEntity.ok(user);
    }


}
