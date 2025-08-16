package com.api.producto.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ConsultaService {

    // consultaId -> {rol, username, sessionId}
    private final Map<String, Map<String, String>> consultas = new HashMap<>();

    public String registrarConsulta(String rol, String username, String sessionId) {
        String consultaId = UUID.randomUUID().toString();
        Map<String, String> data = new HashMap<>();
        data.put("rol", rol);
        data.put("username", username);
        data.put("sessionId", sessionId);
        consultas.put(consultaId, data);
        return consultaId;
    }

    public Map<String, String> obtenerConsulta(String consultaId) {
        return consultas.get(consultaId);
    }
}
