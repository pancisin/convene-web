package com.pancisin.employger.rest.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.employger.models.Company;
import com.pancisin.employger.models.Duty;
import com.pancisin.employger.repository.CompanyRepository;
import com.pancisin.employger.repository.DutyRepository;
import com.pancisin.employger.rest.controllers.exceptions.InvalidRequestException;

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
	
	@RequestMapping(value = "/{company_id}/duties", method = RequestMethod.GET)
	public ResponseEntity<?> getCompanyDuties(@PathVariable Long company_id) {
		Company company = companyRepository.findOne(company_id);
		return ResponseEntity.ok(dutyRepository.findByCompany(company));
	}
	
	@RequestMapping(value = "/{company_id}/duties", method = RequestMethod.POST)
	public ResponseEntity<?> createCompanyDuties(@PathVariable Long company_id, @Valid @RequestBody Duty duty, BindingResult bindingResult) {
		Company company = companyRepository.findOne(company_id);
		duty.setCompany(company);
		
		if (bindingResult.hasErrors()) 
			throw new InvalidRequestException("Invalid data", bindingResult);
		
		return ResponseEntity.ok(dutyRepository.save(duty));
	}
}
