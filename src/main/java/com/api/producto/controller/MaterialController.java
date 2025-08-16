package com.api.producto.controller;

import com.api.producto.model.Material;
import com.api.producto.security.JwtUtil;
import com.api.producto.service.MaterialService;
import com.api.producto.service.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/materiales")
public class MaterialController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MaterialService materialService;
    
    @Autowired
    private ConsultaService consultaService;

    @GetMapping
    public Map<String, Object> listarMateriales(
            @RequestHeader("Authorization") String authHeader,
            @RequestHeader("sessionId") String sessionId) {

        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token);
        String rol = jwtUtil.extractRol(token);

        List<Material> materiales = materialService.obtenerMaterialesPorRol(username, rol);

        String consultaId = consultaService.registrarConsulta(rol, username, sessionId);

        Map<String, Object> response = new HashMap<>();
        response.put("consultaId", consultaId);
        response.put("materiales", materiales);
        return response;
    }
    
    @PostMapping("/filtro")
    public List<Material> filtrarMateriales(
            @RequestHeader("Authorization") String authHeader,
            @RequestHeader("sessionId") String sessionId,
            @RequestBody Map<String, String> filtros) {

        String token = authHeader.replace("Bearer ", "");
        String consultaId = filtros.get("consultaId");

        Map<String, String> consulta = consultaService.obtenerConsulta(consultaId);
        if (consulta == null) {
            throw new RuntimeException("Consulta inválida o expirada");
        }

        // Verificamos que el sessionId coincida con el que generó el consultaId
        if (!consulta.get("sessionId").equals(sessionId)) {
            throw new RuntimeException("SessionId no válido para este consultaId");
        }

        String rol = consulta.get("rol");
        String username = consulta.get("username");

        if (rol.equals("ADMINISTRADOR")) {
            // Admin puede pasar filtros más amplios
            String local = filtros.get("local");
            String material = filtros.get("material");
            return materialService.filtrarComoAdmin(local, material);
        } else {
            // Inventariador solo puede filtrar por material en su local
            String material = filtros.get("material");
            return materialService.filtrarComoInventariador(username, material);
        }
    }


}

