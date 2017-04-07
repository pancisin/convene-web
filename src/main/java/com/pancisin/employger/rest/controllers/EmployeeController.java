package com.pancisin.employger.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.employger.models.Employee;
import com.pancisin.employger.repository.EmployeeRepository;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

	@Autowired
	private EmployeeRepository employeeRepository;

	@GetMapping("/")
	public ResponseEntity<?> getEmployees() {
		return ResponseEntity.ok(employeeRepository.findAll());
	}

	@DeleteMapping("/{employee_id}")
	public ResponseEntity<?> deleteEmployee(@PathVariable Long employee_id) {
		Employee emp = employeeRepository.findOne(employee_id);
		employeeRepository.delete(emp);
		return ResponseEntity.ok("success");
	}
}
