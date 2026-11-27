package com.example.spring.domain.task;

/**
 * Datos necesarios para crear una tarea.
 * @param name Nombre de la tarea.
 * @param done Estado de la tarea.
 */
public record CreateTask(
        String name,
        Boolean done
) {
}
