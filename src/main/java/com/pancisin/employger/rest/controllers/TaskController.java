package com.pancisin.employger.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.employger.models.Task;
import com.pancisin.employger.repository.TaskRepository;

@RestController
@PreAuthorize("hasPermission(#task_id, 'task', '')")
@RequestMapping("/api/task/{task_id}")
public class TaskController {

	@Autowired
	private TaskRepository taskRepository;
	
	@GetMapping
	public ResponseEntity<?> getTask(@PathVariable Long task_id) {
		return ResponseEntity.ok(taskRepository.findOne(task_id)); 
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteTask(@PathVariable Long task_id) {
		taskRepository.delete(task_id);
		return ResponseEntity.ok("success");
	}
	
	@PutMapping
	public ResponseEntity<?> putTask(@PathVariable Long task_id, @RequestBody Task task) {
		return ResponseEntity.ok(taskRepository.save(task));
	}
}
