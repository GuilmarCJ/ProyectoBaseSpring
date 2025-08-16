package com.api.producto.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "materiales_def")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Material {

    @Id
    private Integer id;

    
    private String material;
    private String descripcion;
    private String almacen;
    private String lote;
    private Double cantidad;
    private Double conteo;
    private Double reconteo;
    private LocalDateTime fecReg;
    private String obs;
    private String local;
    private String umb;
    private String parihuela;
    private String ubicacion;
    private LocalDateTime fec;
    private String cta;
    private String usuario;
    private LocalDateTime fecSys;
    private String estado;
}
