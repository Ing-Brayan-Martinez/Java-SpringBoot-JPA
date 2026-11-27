package com.example.spring.web;

import com.example.spring.domain.task.CreateTask;
import com.example.spring.domain.task.Task;
import com.example.spring.domain.task.UpdateTask;
import com.example.spring.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para el recurso de tareas.
 */
@RestController
@RequestMapping("/api/v1")
public final class TaskController {

    /**
     * Servicio de gestión de tareas.
     */
    private final TaskService taskService;

    /**
     * Constructor del controlador.
     * @param service Servicio de tareas.
     */
    @Autowired
    public TaskController(final TaskService service) {
        this.taskService = service;
    }

    /**
     * Crea una nueva tarea.
     * @param task Datos de la tarea a crear.
     * @return Respuesta con la tarea creada o 400 Bad Request.
     */
    @PostMapping("/task")
    public ResponseEntity<Task> save(@RequestBody final CreateTask task) {
        return this.taskService.save(task)
                .map(created -> ResponseEntity.status(
                        HttpStatus.CREATED).body(created))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Actualiza una tarea existente.
     * @param task Datos actualizados de la tarea.
     * @return Respuesta con la tarea actualizada o 404 Not Found.
     */
    @PutMapping("/task")
    public ResponseEntity<Task> update(@RequestBody final UpdateTask task) {
        if (task == null) {
            return ResponseEntity.badRequest().build();
        }
        return this.taskService.edit(task)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Elimina una tarea por su identificador.
     * @param id Identificador de la tarea.
     * @return 204 No Content si se eliminó o 404 Not Found si no existe.
     */
    @DeleteMapping("/task/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable final UUID id) {
        return this.taskService.delete(id)
                .filter(e -> e)
                .map(e -> ResponseEntity.noContent().<Boolean>build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todas las tareas.
     * @return Lista con las tareas disponibles.
     */
    @GetMapping("/task")
    public List<Task> index() {
        return this.taskService.findAllTasks();
    }

    /**
     * Obtiene una tarea por su identificador.
     * @param id Identificador de la tarea.
     * @return Respuesta con la tarea encontrada o 404 Not Found.
     */
    @GetMapping("/task/{id}")
    public ResponseEntity<Task> findById(@PathVariable final UUID id) {
        return this.taskService.findTaskById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
