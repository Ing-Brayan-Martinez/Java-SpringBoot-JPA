package com.example.spring.domain.task;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Representa la entidad de dominio para las tareas.
 */
@Data
@Entity
@Table(name = "task", schema = "public")
public final class Task {

    /**
     * Longitud máxima permitida para el nombre.
     */
    private static final int NAME_MAX_LENGTH = 100;

    /**
     * Variable con el id de la tarea.
     */
    @Id
    private UUID taskId;

    /**
     * Variable con el nombre de la tarea.
     */
    @Column(nullable = false, length = NAME_MAX_LENGTH)
    private String name;

    /**
     * Estado de realización de la tarea.
     */
    @Column(nullable = false)
    private Boolean done;

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
