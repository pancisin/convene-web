package com.pancisin.employger.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pancisin.employger.models.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
