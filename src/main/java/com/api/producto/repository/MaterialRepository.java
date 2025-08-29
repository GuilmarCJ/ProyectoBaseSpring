package com.api.producto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.producto.model.Material;



public interface MaterialRepository extends JpaRepository<Material, Integer> {

    // Consultas básicas que ya tenías
    List<Material> findByLocal(String local);
    List<Material> findByMaterial(String material);
    List<Material> findByLocalAndMaterial(String local, String material);

    // 🔹 Consulta dinámica con filtros opcionales
    @Query("SELECT m FROM Material m " +
           "WHERE (:local IS NULL OR m.local = :local) " +
           "AND (:material IS NULL OR LOWER(m.material) LIKE LOWER(CONCAT('%', :material, '%'))) " +
           "AND (:id IS NULL OR m.id = :id) " +
           "AND (:descripcion IS NULL OR LOWER(m.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%'))) " +
           "AND (:cantidad IS NULL OR m.cantidad = :cantidad)")
    List<Material> buscarMateriales(
            @Param("local") String local,
            @Param("material") String material,
            @Param("id") Integer id,
            @Param("descripcion") String descripcion,
            @Param("cantidad") Integer cantidad
    );
}