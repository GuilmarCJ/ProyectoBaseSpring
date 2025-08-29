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
            @RequestHeader("Authorization") String authHeader,   //  recibe el token 
            @RequestBody Map<String, Object> request) {          // recibe el cuerpo de la petición como un mapa (JSON con materialId, conteo, obs, local)

        //  1. Extrae el token (sin el prefijo "Bearer ")
        String token = authHeader.replace("Bearer ", "");

        //  2. Obtiene datos del usuario desde el JWT
        String username = jwtUtil.extractUsername(token); 
        String rol = jwtUtil.extractRol(token);           

        //  3. Extrae parámetros del body de la petición
        Integer materialId = (Integer) request.get("materialId");  
        Double cantidadContada = Double.valueOf(request.get("conteo").toString()); 
        String obs = (String) request.getOrDefault("obs", null);   

        //  4. Busca el usuario que está haciendo el conteo
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        //  5. Define el "local" dependiendo del rol
        String local;
        if (rol.equals("ADMINISTRADOR")) {
            local = (String) request.get("local");  //  si es admin, puede elegir el local que envíe en la petición
        } else {
            local = usuario.getLocal();             //  si es inventariador, se le fuerza su propio local
        }

        //  6. Busca el material en la base de datos por ID y local
        Material material = materialService.buscarPorIdYLocal(materialId, local);

        //  7. Actualiza los campos del material con la información del conteo
        material.setConteo(cantidadContada);              
        material.setObs(obs);                             
        material.setFecReg(LocalDateTime.now());          
        material.setUsuario(usuario.getNombreCompleto()); // guarda el nombre del usuario que hizo el conteo

        // 👉 8. Guarda el material actualizado en la base de datos y lo devuelve como respuesta
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
        material.setUsuario(usuario.getNombreCompleto()); 

        return materialService.guardar(material);
    }

}