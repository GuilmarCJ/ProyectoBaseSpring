package com.api.producto.controller;

import com.api.producto.model.Usuario;
import com.api.producto.repository.UsuarioRepository;
import com.api.producto.security.JwtUtil;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;
    
 // ----------------- Listar Inventariadores -----------------
    @GetMapping("/listar")
    public ResponseEntity<?> listarUsuarios(@RequestHeader("Authorization") String authHeader) {
        if (!esAdmin(authHeader)) {
            return ResponseEntity.status(403).body("Acceso denegado");
        }
        return ResponseEntity.ok(usuarioRepository.findAll()
                .stream()
                .filter(u -> u.getRol() == Usuario.Rol.INVENTARIADOR) // 👈 solo inventariadores
                .toList());
    }


    // ----------------- Crear Inventariador -----------------
    @PostMapping("/crear")
    public ResponseEntity<?> crearUsuario(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Usuario nuevoUsuario) {

        if (!esAdmin(authHeader)) {
            return ResponseEntity.status(403).body("Acceso denegado");
        }

        nuevoUsuario.setRol(Usuario.Rol.INVENTARIADOR); // 👈 solo se crean inventariadores
        Usuario guardado = usuarioRepository.save(nuevoUsuario);
        return ResponseEntity.ok(guardado);
    }

    // ----------------- Modificar Inventariador -----------------
    @PutMapping("/modificar/	")
    public ResponseEntity<?> modificarUsuario(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer id,
            @RequestBody Usuario datos) {

        if (!esAdmin(authHeader)) {
            return ResponseEntity.status(403).body("Acceso denegado");
        }

        Optional<Usuario> existente = usuarioRepository.findById(id);
        if (existente.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        }

        Usuario usuario = existente.get();
        if (usuario.getRol() != Usuario.Rol.INVENTARIADOR) {
            return ResponseEntity.badRequest().body("Solo se pueden modificar inventariadores");
        }

        usuario.setNombreCompleto(datos.getNombreCompleto());
        usuario.setLocal(datos.getLocal());
        usuario.setAlmacen(datos.getAlmacen());
        usuario.setPassword(datos.getPassword()); // ⚠️ en real: encriptar!
        Usuario actualizado = usuarioRepository.save(usuario);

        return ResponseEntity.ok(actualizado);
    }

    // ----------------- Eliminar Inventariador -----------------
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarUsuario(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer id) {

        if (!esAdmin(authHeader)) {
            return ResponseEntity.status(403).body("Acceso denegado");
        }

        Optional<Usuario> existente = usuarioRepository.findById(id);
        if (existente.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        }

        Usuario usuario = existente.get();
        if (usuario.getRol() != Usuario.Rol.INVENTARIADOR) {
            return ResponseEntity.badRequest().body("Solo se pueden eliminar inventariadores");
        }

        usuarioRepository.delete(usuario);
        return ResponseEntity.ok("Inventariador eliminado");
    }

    // ----------------- Helper -----------------
    private boolean esAdmin(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String rol = jwtUtil.extractRol(token);
        return rol.equals("ADMINISTRADOR");
    }
}
