package com.pancisin.employger.rest.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.employger.models.Customer;
import com.pancisin.employger.repository.CustomerRepository;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

	@Autowired
	private CustomerRepository customerRespository;
	
	@GetMapping("/")
	public ResponseEntity<?> getEmployees() {
		return ResponseEntity.ok(customerRespository.findAll());
	}

	@GetMapping("/{customer_id}")
	public ResponseEntity<?> getCustomer(@PathVariable Long customer_id) {
		return ResponseEntity.ok(customerRespository.findOne(customer_id));
	}
	
	@PutMapping("/{customer_id}")
	public ResponseEntity<?> putCustomer(@PathVariable Long customer_id, @RequestBody @Valid Customer customer) {
		Customer stored = customerRespository.findOne(customer_id);
		
		stored.setName(customer.getName());
		stored.setAddress(customer.getAddress());
		stored.setDescription(customer.getDescription());
		stored.setEmail(customer.getEmail());
		stored.setPerson(customer.getName());
		
		stored.setReport(customer.getReport());
		
		customerRespository.save(stored);
		return null;
	}
		
	@DeleteMapping("/{customer_id}")
	public ResponseEntity<?> deleteCustomer(@PathVariable Long customer_id) {
		Customer customer = customerRespository.findOne(customer_id);
		customerRespository.delete(customer);
		return ResponseEntity.ok("success");
	}
}
