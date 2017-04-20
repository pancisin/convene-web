package com.pancisin.employger.rest.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.employger.models.Duty;
import com.pancisin.employger.models.DutyClause;
import com.pancisin.employger.models.Employee;
import com.pancisin.employger.repository.DutyClauseRepository;
import com.pancisin.employger.repository.DutyRepository;
import com.pancisin.employger.repository.EmployeeRepository;
import com.pancisin.employger.websocket.components.Notifier;

@RestController
@PreAuthorize("hasPermission(#duty_id, 'duty', '')")
@RequestMapping("/api/duty/{duty_id}")
public class DutyController {

	@Autowired
	private Notifier notifier;

	@Autowired
	private DutyRepository dutyRepository;

	@Autowired
	private DutyClauseRepository clauseRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@GetMapping
	public ResponseEntity<?> getDuty(@PathVariable Long duty_id) {
		return ResponseEntity.ok(dutyRepository.findOne(duty_id));
	}

	@PutMapping
	public ResponseEntity<?> updateDuty(@PathVariable Long duty_id, @Valid @RequestBody Duty duty) {
		Duty new_duty = dutyRepository.findOne(duty_id);
		new_duty.setLocation(duty.getLocation());
		new_duty.setStartDate(duty.getStartDate());
		new_duty.setEndDate(duty.getEndDate());
		new_duty.setRecurrence(duty.getRecurrence());
		new_duty.setDescription(duty.getDescription());
		new_duty.setCustomer(duty.getCustomer());

		new_duty.getEmployees().clear();

		for (Employee emp : duty.getEmployees()) {
			Employee new_emp = employeeRepository.findOne(emp.getId());
			new_duty.getEmployees().add(new_emp);
		}

		// notifier.notifyCompany(new_duty.getCompany(), "Duty Updated", "just
		// testing");

		dutyRepository.save(new_duty);
		return ResponseEntity.ok(new_duty);
	}

	@GetMapping("/clause")
	public ResponseEntity<?> getClauses(@PathVariable Long duty_id) {
		return ResponseEntity.ok(dutyRepository.findOne(duty_id).getClauses());
	}

	@RequestMapping(value = "/clause", method = { RequestMethod.POST, RequestMethod.PUT })
	public ResponseEntity<?> postClause(@PathVariable Long duty_id, @RequestBody DutyClause clause) {
		Duty duty = dutyRepository.findOne(duty_id);
		clause.setDuty(duty);
		return ResponseEntity.ok(clauseRepository.save(clause));
	}
}
