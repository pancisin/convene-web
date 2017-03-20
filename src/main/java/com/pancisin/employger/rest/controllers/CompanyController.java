package com.pancisin.employger.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.employger.models.Company;
import com.pancisin.employger.repository.CompanyRepository;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

	@Autowired
	private CompanyRepository companyRepository;
	
	@RequestMapping("/")
	public ResponseEntity<?> getCompanies() {
		return ResponseEntity.ok(companyRepository.findAll());
	}
	
	@RequestMapping("/{company_id}")
	public ResponseEntity<?> getCompany(@PathVariable Long company_id) {
		return ResponseEntity.ok(companyRepository.findOne(company_id));
	}
	
	@RequestMapping("/{company_id}/licenses")
	public ResponseEntity<?> getCompanyLicenses(@PathVariable Long company_id) {
		Company company = companyRepository.findOne(company_id);
		return ResponseEntity.ok(company.getLicenses());
	}
	
}
