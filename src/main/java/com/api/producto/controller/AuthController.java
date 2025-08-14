package com.api.producto.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.api.producto.model.Usuario;
import com.api.producto.repository.UsuarioRepository;
import com.api.producto.security.JwtUtil;


@RestController
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        Usuario usuario = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }

        String token = jwtUtil.generateToken(usuario.getUsername(), usuario.getRol().name());
        String sessionId = UUID.randomUUID().toString();

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("sessionId", sessionId);
        response.put("rol", usuario.getRol().name());

        return ResponseEntity.ok(response);
    }
}

