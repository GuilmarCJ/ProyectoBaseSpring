package com.api.producto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.api.producto.model.Material;



public interface MaterialRepository extends JpaRepository<Material, Integer> {
    List<Material> findByLocal(String local);
    List<Material> findByMaterial(String material);
    List<Material> findByLocalAndMaterial(String local, String material);

}