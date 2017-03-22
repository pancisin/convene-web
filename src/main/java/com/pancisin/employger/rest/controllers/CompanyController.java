package com.pancisin.employger.rest.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.employger.models.Company;
import com.pancisin.employger.models.Duty;
import com.pancisin.employger.models.Employee;
import com.pancisin.employger.repository.CompanyRepository;
import com.pancisin.employger.repository.DutyRepository;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private DutyRepository dutyRepository;
	
	@RequestMapping("/")
	public ResponseEntity<?> getCompanies() {
		return ResponseEntity.ok(companyRepository.findAll());
	}
	
	@RequestMapping("/{company_id}")
	public ResponseEntity<?> getCompany(@PathVariable Long company_id) {
		return ResponseEntity.ok(companyRepository.findOne(company_id));
	}
	
	@RequestMapping("/{company_id}/employees")
	public ResponseEntity<?> getEmployees(@PathVariable Long company_id) {
		Company company = companyRepository.findOne(company_id);
		return ResponseEntity.ok(company.getEmployees());
	}
	
	@RequestMapping("/{company_id}/licenses")
	public ResponseEntity<?> getCompanyLicenses(@PathVariable Long company_id) {
		Company company = companyRepository.findOne(company_id);
		return ResponseEntity.ok(company.getLicenses());
	}
	
	@RequestMapping("/{company_id}/duties")
	public ResponseEntity<?> getCompanyDuties(@PathVariable Long company_id) {
		Company company = companyRepository.findOne(company_id);
		
		Set<Duty> result = new HashSet<Duty>();
		
		for(Employee emp : company.getEmployees()) {
			result.addAll(emp.getDuties());
		}
		
		return ResponseEntity.ok(result);
	}
}
