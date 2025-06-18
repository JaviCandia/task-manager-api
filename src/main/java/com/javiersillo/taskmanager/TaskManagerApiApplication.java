package com.javiersillo.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
public class TaskManagerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagerApiApplication.class, args);
    }

}
