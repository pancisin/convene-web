package com.pancisin.employger.rest.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.pancisin.employger.models.Company;
import com.pancisin.employger.models.Duty;
import com.pancisin.employger.models.Employee;
import com.pancisin.employger.repository.CompanyRepository;
import com.pancisin.employger.repository.DutyRepository;
import com.pancisin.employger.repository.EmployeeRepository;
import com.pancisin.employger.rest.controllers.exceptions.InvalidRequestException;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

	@Autowired
	ServletContext servletContext;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private DutyRepository dutyRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@GetMapping("/")
	public ResponseEntity<?> getCompanies() {
		return ResponseEntity.ok(companyRepository.findAll());
	}

	@GetMapping("/{company_id}")
	public ResponseEntity<?> getCompany(@PathVariable Long company_id) {
		return ResponseEntity.ok(companyRepository.findOne(company_id));
	}

	@PutMapping(value = "/{company_id}")
	public ResponseEntity<?> updateCompany(@PathVariable Long company_id, @RequestBody Company company) {
		Company stored = companyRepository.findOne(company_id);

		if (stored == null) {
			return null;
		}

		if (Pattern.compile("^data:image/[^;]*;base64,?").matcher(company.getLogo()).find()) {
			String relative_path = "resources/logos/" + company.getIco().toString() + ".jpg";

			File file = new File(servletContext.getRealPath("/") + relative_path);
			file.getParentFile().mkdirs();

			try (FileOutputStream imageOutFile = new FileOutputStream(file)) {
				String imageData = company.getLogo().replaceFirst("^data:image/[^;]*;base64,?", "");
				byte[] imageByteArray = Base64.getDecoder().decode(imageData);
				imageOutFile.write(imageByteArray);
				stored.setLogo("/" + relative_path);
			} catch (FileNotFoundException e) {
				System.out.println("Image not found" + e);
			} catch (IOException ioe) {
				System.out.println("Exception while reading the Image " + ioe);
			}
		}

		stored.setName(company.getName());
		stored.setEmail(company.getEmail());
		stored.setPhoneNumber(company.getPhoneNumber());
		stored.setAddress(company.getAddress());

		companyRepository.save(stored);
		return ResponseEntity.ok(stored);
	}

	@GetMapping("/{company_id}/users")
	public ResponseEntity<?> getUsers(@PathVariable Long company_id) {
		Company company = companyRepository.findOne(company_id);
		return ResponseEntity.ok(company.getUsers());
	}

	@GetMapping("/{company_id}/employees")
	public ResponseEntity<?> getEmployees(@PathVariable Long company_id) {
		Company company = companyRepository.findOne(company_id);
		return ResponseEntity.ok(company.getEmployees());
	}

	@PostMapping("/{company_id}/employees")
	public ResponseEntity<?> postEmployee(@PathVariable Long company_id, @RequestBody @Valid Employee employee,
			BindingResult bindingResult) {
		Company company = companyRepository.findOne(company_id);
		
		if (bindingResult.hasErrors())
			throw new InvalidRequestException("Invalid data", bindingResult);
		
		employee.setCompany(company);
		return ResponseEntity.ok(employeeRepository.save(employee));
	}

	@GetMapping("/{company_id}/licenses")
	public ResponseEntity<?> getCompanyLicenses(@PathVariable Long company_id) {
		Company company = companyRepository.findOne(company_id);
		return ResponseEntity.ok(company.getLicenses());
	}

	@GetMapping("/{company_id}/duties")
	public ResponseEntity<?> getCompanyDuties(@PathVariable Long company_id) {
		Company company = companyRepository.findOne(company_id);
		return ResponseEntity.ok(company.getDuties());
	}

	@GetMapping("/{company_id}/duties/{date_to}")
	public ResponseEntity<?> getDutiesInOccurrence(@PathVariable Long company_id,
			@PathVariable @DateTimeFormat(iso = ISO.DATE) String date_to) {
		Company company = companyRepository.findOne(company_id);
		List<Duty> result = new ArrayList<Duty>();

		ZonedDateTime now = ZonedDateTime.now();
		CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX));
		Duration week = Duration.ofDays(7);

		for (Duty duty : company.getDuties()) {
			ExecutionTime ex = ExecutionTime.forCron(parser.parse(duty.getCronRecurrence()));
			Duration dur = ex.timeToNextExecution(now);

			if (week.compareTo(dur) == 1)
				result.add(duty);
		}

		return ResponseEntity.ok(result);
	}

	@PostMapping("/{company_id}/duties")
	public ResponseEntity<?> createCompanyDuties(@PathVariable Long company_id, @Valid @RequestBody Duty duty,
			BindingResult bindingResult) {
		Company company = companyRepository.findOne(company_id);

		if (bindingResult.hasErrors())
			throw new InvalidRequestException("Invalid data", bindingResult);

		duty.setCompany(company);

		return ResponseEntity.ok(dutyRepository.save(duty));
	}
}
