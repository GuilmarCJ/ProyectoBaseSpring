package com.api.producto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.producto.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsername(String username);
}
