package com.javiersillo.taskmanager.service;

import com.javiersillo.taskmanager.exception.TaskIdMismatchException;
import com.javiersillo.taskmanager.exception.TaskNotFoundException;
import com.javiersillo.taskmanager.model.Task;
import com.javiersillo.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with Id: " + id + " not found"));
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task task) {
        if(task.getId() != null && !task.getId().equals(id)) {
            throw new TaskIdMismatchException("Path ID does not match Task ID");
        }

        Task existingTask = getTaskById(id);
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setCompleted(task.isCompleted());
        existingTask.setUpdatedAt(task.getUpdatedAt());

        return taskRepository.save(existingTask);
    }

    public void deleteTaskById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task with Id: " + id + " not found");
        }

        taskRepository.deleteById(id);
    }

    public List<Task> getCompletedTasks() {
        return taskRepository.findByCompletedTrue();
    }

    public List<Task> getTasksByTitle(String title) {
        return taskRepository.findAllByTitle(title);
    }
}
