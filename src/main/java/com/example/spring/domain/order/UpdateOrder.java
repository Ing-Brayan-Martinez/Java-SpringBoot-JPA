package com.example.spring.domain.order;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Datos necesarios para actualizar una orden existente.
 * @param orderId Identificador de la orden.
 * @param name Nombre de la orden.
 * @param amount Monto de la orden.
 * @param discount Descuento aplicado a la orden.
 */
public record UpdateOrder(
        UUID orderId,
        String name,
        BigDecimal amount,
        BigDecimal discount
) {
}
