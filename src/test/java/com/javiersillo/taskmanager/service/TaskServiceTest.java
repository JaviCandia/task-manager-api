package com.javiersillo.taskmanager.service;

import com.javiersillo.taskmanager.model.Task;
import com.javiersillo.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    void getAllTasks() {
        // ARRANGE: setup test data and mock behavior
        Task task1 = Task.builder().id(1L).title("Task 1").completed(false).createdAt(LocalDateTime.now()).build();
        Task task2 = Task.builder().id(2L).title("Task 2").completed(false).createdAt(LocalDateTime.now()).build();

        // Configure the mock repository to return the tasks when findAll() is called
        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));

        // ACT: Execute the method under test
        List<Task> result = taskService.getAllTasks();

        // ASSERT: Verify the outcome
        // Check if the returned list size is correct
        assertEquals(2, result.size());

        // Verify that repository's findAll() method was actually called
        verify(taskRepository).findAll();
    }

    @Test
    void getTaskById() {
    }
}
