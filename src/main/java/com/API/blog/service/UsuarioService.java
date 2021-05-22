package com.API.blog.service;

import com.API.blog.domain.Usuario;
import com.API.blog.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class UsuarioService {
    public final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService (UsuarioRepository usuarioRepository){
        this.usuarioRepository=usuarioRepository;
    }
    public Usuario newUsuario(Usuario usuario) {
        if (!usuarioRepository.existsById(usuario.getId())) {
            return usuarioRepository.save(usuario);
        }
        else{
            return null;
        }
    }

    public Usuario login(String email, String password) {
        return usuarioRepository.findByEmailAndPassword(email, password);
    }

    public Usuario getUsuarioById(Integer id){
        return usuarioRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public Page allUsuarios(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }




}
