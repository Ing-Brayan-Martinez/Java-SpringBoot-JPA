package com.example.spring.domain.product;

import java.math.BigDecimal;

/**
 * Datos necesarios para crear un producto.
 * @param name Nombre del producto.
 * @param price Precio del producto.
 * @param stock Stock inicial del producto.
 */
public record CreateProduct(
        String name,
        BigDecimal price,
        Integer stock
) {
}
