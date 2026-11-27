package com.example.spring.domain.task;

import java.util.UUID;

/**
 * Datos necesarios para actualizar una tarea existente.
 * @param taskId Identificador de la tarea.
 * @param name Nombre de la tarea.
 * @param done Estado de la tarea.
 */
public record UpdateTask(
        UUID taskId,
        String name,
        Boolean done
) {
}
