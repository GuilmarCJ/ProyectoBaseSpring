package com.api.producto.service;

import com.api.producto.model.Material;
import com.api.producto.repository.MaterialRepository;
import com.api.producto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Material> obtenerMaterialesPorRol(String username, String rol) {
        if (rol.equals("ADMINISTRADOR")) {
            return materialRepository.findAll();
        } else {
            String localUsuario = usuarioRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                    .getLocal();
            return materialRepository.findByLocal(localUsuario);
        }
    }
}