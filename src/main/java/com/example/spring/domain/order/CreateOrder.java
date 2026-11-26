package com.example.spring.domain.order;

import java.math.BigDecimal;

/**
 * Datos necesarios para crear una orden.
 * @param name Nombre de la orden.
 * @param amount Monto de la orden.
 * @param discount Descuento aplicado a la orden.
 */
public record CreateOrder(
        String name,
        BigDecimal amount,
        BigDecimal discount
) {
}
