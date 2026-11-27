package com.example.spring.service;

import com.example.spring.domain.task.CreateTask;
import com.example.spring.domain.task.Task;
import com.example.spring.domain.task.UpdateTask;
import com.example.spring.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio para operaciones de negocio de tareas.
 */
@Service
@CacheConfig(cacheNames = {"taskCache"})
public class TaskService {

    /**
     * Repositorio de tareas.
     */
    private final TaskRepository taskRepository;

    /**
     * Constructor para inyección de dependencias.
     * @param repository Repositorio de tareas.
     */
    @Autowired
    public TaskService(final TaskRepository repository) {
        this.taskRepository = repository;
    }

    /**
     * Método para persistir una tarea.
     * @param task La tarea a persistir.
     * @return Optional con la tarea guardada o vacío si es inválida.
     */
    public Optional<Task> save(final CreateTask task) {
        if (task == null
                || task.name() == null
                || task.name().isBlank()
                || task.done() == null) {
            return Optional.empty();
        }
        final Task entity = new Task();
        entity.setTaskId(UUID.randomUUID());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setName(task.name());
        entity.setDone(task.done());
        return taskRepository.createTask(entity);
    }

    /**
     * Método para actualizar una tarea.
     * @param task La tarea a actualizar.
     * @return Optional con la tarea actualizada o vacío si es inválida.
     */
    public Optional<Task> edit(final UpdateTask task) {
        if (task == null
                || task.taskId() == null
                || task.name() == null
                || task.name().isBlank()
                || task.done() == null) {
            return Optional.empty();
        }
        return taskRepository.findById(task.taskId())
                .flatMap(origin -> {
                    origin.setName(task.name());
                    origin.setDone(task.done());
                    origin.setUpdatedAt(LocalDateTime.now());
                    return taskRepository.updateTask(origin);
                });
    }

    /**
     * Método para eliminar una tarea según su id.
     * @param id El id de la tarea a eliminar.
     * @return Optional con true si existía y se eliminó, o false en otro caso.
     */
    public Optional<Boolean> delete(final UUID id) {
        return taskRepository.existsByTaskId(id)
                .flatMap(exists -> {
                    if (exists) {
                        taskRepository.deleteTaskById(id);
                        return Optional.of(true);
                    }
                    return Optional.of(false);
                });
    }

    /**
     * Método para obtener todas las tareas.
     * @return Retorna una lista con todas las tareas.
     */
    @CacheEvict(value = "taskCache", allEntries = true)
    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Método para obtener una tarea según su id.
     * @param id El id de la tarea.
     * @return Optional con la tarea encontrada.
     */
    @CachePut(value = "taskCache")
    public Optional<Task> findTaskById(final UUID id) {
        return taskRepository.findTaskById(id);
    }
}
