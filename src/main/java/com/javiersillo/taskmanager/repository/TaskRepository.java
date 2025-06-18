package com.javiersillo.taskmanager.repository;

import com.javiersillo.taskmanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByCompletedTrue();

    List<Task> findAllByTitle(String title);
}
