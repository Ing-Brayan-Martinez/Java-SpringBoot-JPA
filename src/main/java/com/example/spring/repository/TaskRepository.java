package com.example.spring.repository;

import com.example.spring.domain.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para la entidad Task.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    /**
     * Persiste una entidad Task en la base de datos.
     * @param task Entidad Task a guardar.
     * @return Optional con la entidad guardada.
     */
    default Optional<Task> createTask(Task task) {
        return Optional.of(save(task));
    }

    /**
     * Actualiza una entidad Task existente.
     * @param task Entidad Task a actualizar.
     * @return Optional con la entidad actualizada.
     */
    default Optional<Task> updateTask(Task task) {
        return Optional.of(save(task));
    }

    /**
     * Elimina una tarea por su ID mediante consulta JPQL.
     * @param id Identificador de la tarea a eliminar.
     */
    @Modifying
    @Query("DELETE FROM Task t WHERE t.taskId = :id")
    void deleteByTaskId(@Param("id") UUID id);

    /**
     * Elimina una tarea según su ID y retorna un Optional vacío.
     * @param id Identificador de la tarea a eliminar.
     * @return Optional con Void.
     */
    default Optional<Void> deleteTaskById(UUID id) {
        deleteByTaskId(id);
        return Optional.empty();
    }

    /**
     * Consulta JPQL para buscar una tarea por su identificador.
     * @param id Identificador de la tarea.
     * @return Optional con la tarea encontrada.
     */
    @Query("SELECT t FROM Task t WHERE t.taskId = :id")
    Optional<Task> findTaskById(@Param("id") UUID id);

    /**
     * Consulta JPQL para verificar si existe una tarea por ID.
     * @param id Identificador de la tarea.
     * @return Optional con verdadero si existe la tarea.
     */
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END "
            + "FROM Task t WHERE t.taskId = :id")
    Optional<Boolean> existsByTaskId(@Param("id") UUID id);
}
