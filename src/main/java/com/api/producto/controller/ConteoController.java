package com.api.producto.controller;

import com.api.producto.model.Material;
import com.api.producto.model.Usuario;
import com.api.producto.repository.UsuarioRepository;
import com.api.producto.security.JwtUtil;
import com.api.producto.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/materiales")
public class ConteoController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MaterialService materialService;

 // ----------------- CONTEO -----------------
    @PostMapping("/conteo")
    public Material hacerConteo(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> request) {

        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);
        String rol = jwtUtil.extractRol(token);

        Integer materialId = (Integer) request.get("materialId");
        Double cantidadContada = Double.valueOf(request.get("conteo").toString());
        String obs = (String) request.getOrDefault("obs", null);

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String local;
        if (rol.equals("ADMINISTRADOR")) {
            local = (String) request.get("local"); // Admin puede escoger
        } else {
            local = usuario.getLocal(); // Inventariador solo su local
        }

        Material material = materialService.buscarPorIdYLocal(materialId, local);
        material.setConteo(cantidadContada);
        material.setObs(obs);
        material.setFecReg(LocalDateTime.now());
        material.setUsuario(usuario.getNombreCompleto()); // 👈 aquí el nombre completo

        return materialService.guardar(material);
    }

    // ----------------- RECONTEO -----------------
    @PostMapping("/reconteo")
    public Material hacerReconteo(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> request) {

        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);
        String rol = jwtUtil.extractRol(token);

        Integer materialId = (Integer) request.get("materialId");
        Double cantidadRecontada = Double.valueOf(request.get("reconteo").toString());
        String obs = (String) request.getOrDefault("obs", null);

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String local;
        if (rol.equals("ADMINISTRADOR")) {
            local = (String) request.get("local"); // Admin puede escoger
        } else {
            local = usuario.getLocal(); // Inventariador solo su local
        }

        Material material = materialService.buscarPorIdYLocal(materialId, local);
        material.setReconteo(cantidadRecontada);
        material.setObs(obs);
        material.setFecReg(LocalDateTime.now());
        material.setUsuario(usuario.getNombreCompleto()); // 👈 aquí también

        return materialService.guardar(material);
    }

}