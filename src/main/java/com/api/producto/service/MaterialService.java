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
    
    public List<Material> filtrarComoAdmin(String local, String material, Integer id, String descripcion, Integer cantidad) {
        return materialRepository.buscarMateriales(local, material, id, descripcion, cantidad);
    }

    public List<Material> filtrarComoInventariador(String username, String material, Integer id, String descripcion, Integer cantidad) {
        String localUsuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getLocal();

        return materialRepository.buscarMateriales(localUsuario, material, id, descripcion, cantidad);
    }


    
    public Material buscarPorIdYLocal(Integer id, String local) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material no encontrado"));
        if (!material.getLocal().equals(local)) {
            throw new RuntimeException("No tienes permiso para modificar este material");
        }
        return material;
    }

    public Material guardar(Material material) {
        return materialRepository.save(material);
    }

    
    
}