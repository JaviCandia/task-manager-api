package com.javiersillo.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javiersillo.taskmanager.model.Task;
import com.javiersillo.taskmanager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        task1 = Task.builder()
                .id(1L)
                .title("Task 1")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .build();

        task2 = Task.builder()
                .id(2L)
                .title("Task 2")
                .completed(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void getAllTasks_shouldReturnAllTask() throws Exception {
        // ARRANGE
        when(taskService.getAllTasks()).thenReturn(List.of(task1, task2));


        // ACT + ASSERT
        mockMvc.perform(get("/api/tasks")
                        .with(user("testuser").roles("USER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].title").value("Task 2"));
    }

    @Test
    void getTaskById_shouldReturnOneTask() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(task1);

        mockMvc.perform(get("/api/tasks/1")
                        .with(user("testuser").roles("USER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void createATask_shouldReturnCreatedTask() throws Exception {
        Task newTask = Task.builder()
                .title("Task 1")
                .completed(false)
                .build();

        when(taskService.createTask(Mockito.any(Task.class))).thenReturn(task1);

        mockMvc.perform(post("/api/tasks")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(newTask))
                        .with(user("testuser").roles("USER"))
                        .with(csrf())
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(task1.getId()))
                .andExpect(jsonPath("$.title").value(task1.getTitle()))
                .andExpect(jsonPath("$.completed").value(task1.isCompleted()));
    }

    @Test
    void updateTask_shouldReturnUpdatedTask() throws Exception {
        Task inputTask = Task.builder()
                .title("Task 1 Updated")
                .completed(true)
                .build();

        Task updatedTask = Task.builder()
                .id(1L)
                .title("Task 1 Updated")
                .completed(true)
                .createdAt(task1.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        when(taskService.updateTask(eq(1L), Mockito.any(Task.class))).thenReturn(updatedTask);

        mockMvc.perform(patch("/api/tasks/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(inputTask))
                        .with(user("testuser").roles("USER"))
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedTask.getId()))
                .andExpect(jsonPath("$.title").value(updatedTask.getTitle()))
                .andExpect(jsonPath("$.completed").value(updatedTask.isCompleted()));
    }

    @Test
    void deleteTask_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/tasks/{id}", 1L)
                        .with(user("testuser").roles("USER"))
                        .with(csrf())
                )
                .andExpect(status().isNoContent());

        verify(taskService, Mockito.timeout(1)).deleteTaskById(1L);
    }

    @Test
    void getCompletedTask_shouldReturnCompletedTasks() throws Exception {
        when(taskService.getCompletedTasks()).thenReturn(List.of(task2));

        mockMvc.perform(get("/api/tasks/completed")
                        .with(user("testuser").roles("USER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].completed").value(true));
    }

    @Test
    void getTasksByTitle_shouldReturnTasksByTitle() throws Exception {
        when(taskService.getTasksByTitle(Mockito.anyString())).thenReturn(List.of(task2));

        mockMvc.perform(get("/api/tasks/title")
                        .param("title", "Task 2")
                        .with(user("testuser").roles("USER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value(task2.getTitle()));
    }
}