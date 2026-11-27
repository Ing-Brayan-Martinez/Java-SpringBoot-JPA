package com.example.spring.domain.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

/**
 * Representa la entidad de dominio para las órdenes de compra.
 */
@Data
@Entity
@Table(name = "order", schema = "public")
public final class Order {

    /**
     * Longitud máxima permitida para el nombre.
     */
    private static final int NAME_MAX_LENGTH = 100;

    /**
     * Variable con el id de la orden.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, columnDefinition = "timestamp default now()")
    private LocalDateTime createdAt;

    /**
     * Variable con la fecha de actualización.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, columnDefinition = "timestamp default now()")
    private LocalDateTime updatedAt;
}
