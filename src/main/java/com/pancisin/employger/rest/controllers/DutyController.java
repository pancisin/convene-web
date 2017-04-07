package com.pancisin.employger.rest.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.employger.models.Duty;
import com.pancisin.employger.models.Employee;
import com.pancisin.employger.repository.DutyRepository;
import com.pancisin.employger.repository.EmployeeRepository;
import com.pancisin.employger.rest.controllers.exceptions.InvalidRequestException;

@RestController
@RequestMapping("/api/duty")
public class DutyController {
	
	@Autowired
	private DutyRepository dutyRepository;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<?> getDuties() {
		return ResponseEntity.ok(dutyRepository.findAll());
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<?> createDuty(@Valid @RequestBody Duty duty, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) 
			throw new InvalidRequestException("Invalid data", bindingResult);
		
		return ResponseEntity.ok(dutyRepository.save(duty));
	}
	
	@RequestMapping(value = "/{duty_id}", method = RequestMethod.GET)
	public ResponseEntity<?> getDuty(@PathVariable Long duty_id) {
		return ResponseEntity.ok(dutyRepository.findOne(duty_id));
	}
	
	@RequestMapping(value = "/{duty_id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateDuty(@PathVariable Long duty_id, @Valid @RequestBody Duty duty) {
		Duty new_duty = dutyRepository.findOne(duty_id);
		new_duty.setLocation(duty.getLocation());
		new_duty.setStartDate(duty.getStartDate());
		new_duty.setEndDate(duty.getEndDate());
		new_duty.setRecurrence(duty.getRecurrence());
		new_duty.setDescription(duty.getDescription());
		
		new_duty.getEmployees().clear();
		
		for(Employee emp : duty.getEmployees()) {
			Employee new_emp = employeeRepository.findOne(emp.getId());
			new_duty.getEmployees().add(new_emp);
		}

		dutyRepository.save(new_duty);
		return ResponseEntity.ok(new_duty);
	}
}
