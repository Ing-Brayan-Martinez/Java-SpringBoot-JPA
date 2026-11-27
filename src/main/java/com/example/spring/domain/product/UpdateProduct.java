package com.example.spring.domain.product;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Datos necesarios para actualizar un producto existente.
 * @param productId Identificador del producto.
 * @param name Nombre del producto.
 * @param price Precio del producto.
 * @param stock Stock del producto.
 */
public record UpdateProduct(
        UUID productId,
        String name,
        BigDecimal price,
        Integer stock
) {
}
