package com.javiersillo.taskmanager.service;

import com.javiersillo.taskmanager.exception.TaskIdMismatchException;
import com.javiersillo.taskmanager.exception.TaskNotFoundException;
import com.javiersillo.taskmanager.model.Task;
import com.javiersillo.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    void testGetAllTasks() {
        // ARRANGE: setup test data and mock behavior
        Task task1 = Task.builder()
                .id(1L)
                .title("Task 1")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .build();

        Task task2 = Task.builder()
                .id(2L)
                .title("Task 2")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .build();

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
    void testGetTaskById_ExistingId() {
        Task task = Task.builder()
                .id(1L)
                .title("My Task")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(1L);

        assertEquals("My Task", result.getTitle());
    }

    @Test
    void testGetTaskById_NonExistingId() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(99L));
    }

    @Test
    void testCreateTask() {
        Task newTask = Task.builder()
                .title("New Task")
                .completed(false)
                .build();

        Task savedTask = Task.builder()
                .id(1L)
                .title("New Task")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(taskRepository.save(newTask)).thenReturn(savedTask);

        Task result = taskService.createTask(newTask);

        assertNotNull(result.getId());
        assertEquals("New Task", result.getTitle());
        verify(taskRepository).save(newTask);
    }

    @Test
    void testUpdateTask_Successful() {
        Long id = 1L;

        Task existingTask = Task.builder()
                .id(id)
                .title("Old Title")
                .description("Old Desc")
                .completed(false)
                .dueDate(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Task input = Task.builder()
                .id(id)
                .title("New Title")
                .description("New Description")
                .completed(true)
                .dueDate(LocalDateTime.now().plusDays(2))
                .updatedAt(LocalDateTime.now())
                .build();

        when(taskRepository.findById(id)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        Task result = taskService.updateTask(id, input);

        assertEquals("New Title", result.getTitle());
        assertTrue(result.isCompleted());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void testUpdateTask_IdMismatch() {
        Task input = Task.builder()
                .id(99L)
                .title("Wrong ID")
                .build();

        // TODO: I need to deep dive into lambda functions
        assertThrows(TaskIdMismatchException.class, () -> taskService.updateTask(1L, input));
    }

    @Test
    void testDeleteTaskById_ExistingId() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        // TODO: I need to deep dive into doNothing
        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteTaskById(1L);

        verify(taskRepository).deleteById(1L);
    }

    @Test
    void testDeleteTaskById_NonExistingId() {
        when(taskRepository.existsById(99L)).thenReturn(false);

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTaskById(99L));
    }

    @Test
    void testGetCompletedTasks() {
        Task task1 = Task.builder()
                .id(1L)
                .title("Done 1")
                .completed(true)
                .build();

        Task task2 = Task.builder()
                .id(2L)
                .title("Done 2")
                .completed(true)
                .build();

        when(taskRepository.findByCompletedTrue()).thenReturn(List.of(task1, task2));

        List<Task> result = taskService.getCompletedTasks();

        assertEquals(2, result.size());
        assertTrue(result.getFirst().isCompleted());
    }

    @Test
    void testGetTasksbyTitle() {
        String title = "Meeting";
        Task task = Task.builder()
                .id(1L)
                .title(title)
                .completed(false)
                .build();

        when(taskRepository.findAllByTitle(title)).thenReturn(List.of(task));

        List<Task> result = taskService.getTasksByTitle(title);

        assertEquals(1, result.size());
        assertEquals("Meeting", result.getFirst().getTitle());
    }
}