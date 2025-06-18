package com.javiersillo.taskmanager.controller;

import com.javiersillo.taskmanager.model.Task;
import com.javiersillo.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> findAllByTitle(String title) {
        return taskRepository.findAllByTitle(title);
    }
}
