package com.example.spring.domain.order;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Representa la entidad de dominio para las órdenes de compra.
 */
@Data
@Entity
@Table(name = "\"order\"", schema = "public")
public final class Order {

    /**
     * Longitud máxima permitida para el nombre.
     */
    private static final int NAME_MAX_LENGTH = 100;

    /**
     * Variable con el id de la orden.
     */
    @Id
    private UUID orderId;

    /**
     * Variable con el nombre de la orden.
     */
    @Column(nullable = false, length = NAME_MAX_LENGTH)
    private String name;

    /**
     * Variable con el monto de la orden.
     */
    @Column(nullable = false, columnDefinition = "numeric default 0")
    private BigDecimal amount;

    /**
     * Variable con el descuento de la orden.
     */
    @Column(nullable = false, columnDefinition = "numeric default 0")
    private BigDecimal discount;

    /**
     * Variable con la fecha de creacion.
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * Variable con la fecha de actualización.
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
