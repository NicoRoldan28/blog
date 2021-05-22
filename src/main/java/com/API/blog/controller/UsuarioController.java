package com.API.blog.controller;

import com.API.blog.domain.Usuario;
import com.API.blog.dto.LoginRequestDto;
import com.API.blog.dto.LoginResponseDto;
import com.API.blog.dto.UserDto;
import com.API.blog.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.API.blog.util.Constants.JWT_SECRET;

@Slf4j
@RestController
@RequestMapping(value = "/")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    @Autowired
    private UsuarioController(UsuarioService usuarioService,ObjectMapper objectMapper,ModelMapper modelMapper){
        this.usuarioService=usuarioService;
        this.objectMapper=objectMapper;
        this.modelMapper=modelMapper;
    }

    @PostMapping(value = "auth/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        log.info(loginRequestDto.toString());
        Usuario user = usuarioService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        if (user!=null){
            UserDto dto = modelMapper.map(user, UserDto.class);
            return ResponseEntity.ok(LoginResponseDto.builder().token(this.generateToken(dto)).build());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
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

    @GetMapping(value = "/userDetails")
    public ResponseEntity<Usuario> userDetails(Authentication auth) {
        return ResponseEntity.ok((Usuario) auth.getPrincipal());
    }

    private String generateToken(UserDto userDto) {
        try {
            List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("DEFAULT_USER");
            String token = Jwts
                    .builder()
                    .setId("JWT")
                    .setSubject(userDto.getEmail())
                    .claim("user", objectMapper.writeValueAsString(userDto))
                    .claim("authorities",grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 180000))
                    .signWith(SignatureAlgorithm.HS512, JWT_SECRET.getBytes()).compact();
            return  token;
        } catch(Exception e) {
            return "dummy";
        }



    }

}
