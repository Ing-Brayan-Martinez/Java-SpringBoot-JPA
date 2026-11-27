package com.example.spring.domain.product;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Representa la entidad de dominio para los productos.
 */
@Data
@Entity
@Table(name = "product", schema = "public")
public final class Product {

    /**
     * Longitud máxima permitida para el nombre.
     */
    private static final int NAME_MAX_LENGTH = 100;

    /**
     * Variable con el id del producto.
     */
    @Id
    private UUID productId;

    /**
     * Variable con el nombre del producto.
     */
    @Column(nullable = false, length = NAME_MAX_LENGTH)
    private String name;

    /**
     * Precio del producto.
     */
    @Column(nullable = false, columnDefinition = "numeric default 0")
    private BigDecimal price;

    /**
     * Stock disponible del producto.
     */
    @Column(nullable = false)
    private Integer stock;

    /**
     * Variable con la fecha de creación.
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * Variable con la fecha de actualización.
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
