package com.api.producto.controller;

import com.api.producto.model.Material;
import com.api.producto.security.JwtUtil;
import com.api.producto.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materiales")
public class MaterialController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MaterialService materialService;

    @GetMapping
    public List<Material> listarMateriales(
            @RequestHeader("Authorization") String authHeader,
            @RequestHeader("sessionId") String sessionId) {

        // Extraer token del header
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);
        String rol = jwtUtil.extractRol(token);

        // Aquí podrías validar que el sessionId existe en tu almacenamiento de sesiones
        // (en este ejemplo no se guarda en BD, pero puedes hacerlo si quieres)

        return materialService.obtenerMaterialesPorRol(username, rol);
    }
}

