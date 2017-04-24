package com.pancisin.employger.rest.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.employger.models.Attribute;
import com.pancisin.employger.models.Company;
import com.pancisin.employger.models.Customer;
import com.pancisin.employger.models.Duty;
import com.pancisin.employger.models.DutyClause;
import com.pancisin.employger.models.Employee;
import com.pancisin.employger.repository.AttributeRepository;
import com.pancisin.employger.repository.CompanyRepository;
import com.pancisin.employger.repository.CustomerRepository;
import com.pancisin.employger.repository.DutyClauseRepository;
import com.pancisin.employger.repository.DutyRepository;
import com.pancisin.employger.repository.EmployeeRepository;
import com.pancisin.employger.rest.controllers.exceptions.InvalidRequestException;
import com.pancisin.employger.rest.controllers.objects.DutyInstance;

@RestController
@PreAuthorize("hasPermission(#company_id, 'company', '')")
@RequestMapping("/api/company/{company_id}")
public class CompanyController {

	@Autowired
	ServletContext servletContext;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private DutyRepository dutyRepository;

	@Autowired
	private DutyClauseRepository clauseRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AttributeRepository attributeRepository;
	
	@GetMapping()
	public ResponseEntity<?> getCompany(@PathVariable Long company_id) {
		return ResponseEntity.ok(companyRepository.findOne(company_id));
	}

	@PutMapping()
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

	@GetMapping("/users")
	public ResponseEntity<?> getUsers(@PathVariable Long company_id) {
		Company company = companyRepository.findOne(company_id);
		return ResponseEntity.ok(company.getUsers());
	}

	@GetMapping("/employees")
	public ResponseEntity<?> getEmployees(@PathVariable Long company_id) {
		Company company = companyRepository.findOne(company_id);
		return ResponseEntity.ok(company.getEmployees());
	}

	@PostMapping("/employees")
	public ResponseEntity<?> postEmployee(@PathVariable Long company_id, @RequestBody @Valid Employee employee,
			BindingResult bindingResult) {
		Company company = companyRepository.findOne(company_id);

		if (bindingResult.hasErrors())
			throw new InvalidRequestException("Invalid data", bindingResult);

		employee.setCompany(company);
		return ResponseEntity.ok(employeeRepository.save(employee));
	}

	@GetMapping("/licenses")
	public ResponseEntity<?> getCompanyLicenses(@PathVariable Long company_id) {
		Company company = companyRepository.findOne(company_id);
		return ResponseEntity.ok(company.getLicenses());
	}

	@GetMapping("/duties")
	public ResponseEntity<?> getCompanyDuties(@PathVariable Long company_id) {
		Company company = companyRepository.findOne(company_id);
		return ResponseEntity.ok(company.getDuties());
	}

	@GetMapping("/customers")
	public ResponseEntity<?> getCompanyCustomers(@PathVariable Long company_id) {
		Company company = companyRepository.findOne(company_id);
		return ResponseEntity.ok(company.getCustomers());
	}

	@PostMapping("/customers")
	public ResponseEntity<?> postCompanyCustomer(@PathVariable Long company_id, @RequestBody @Valid Customer customer,
			BindingResult bindingResult) {
		Company company = companyRepository.findOne(company_id);

		if (bindingResult.hasErrors())
			throw new InvalidRequestException("Invalid data", bindingResult);

		customer.setCompany(company);
		return ResponseEntity.ok(customerRepository.save(customer));
	}

	@GetMapping("/duties/{date_to}")
	public ResponseEntity<?> getDutiesInOccurrence(@PathVariable Long company_id,
			@PathVariable @DateTimeFormat(iso = ISO.DATE) String date_to) {
		Company company = companyRepository.findOne(company_id);
		List<Duty> result = new ArrayList<Duty>();

		Date dateTo;
		try {
			dateTo = new SimpleDateFormat("y-M-d").parse(date_to);
			for (Duty duty : company.getDuties()) {
				List<Date> occ = duty.getNextOcurrences(1, new Date());
				if (occ.size() > 0 && occ.get(0).before(dateTo))
					result.add(duty);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok(result);
	}

	@GetMapping("/instances/{date_from}/{date_to}")
	public ResponseEntity<?> getDutyInstances(@PathVariable Long company_id,
			@PathVariable @DateTimeFormat(iso = ISO.DATE) String date_to,
			@PathVariable @DateTimeFormat(iso = ISO.DATE) String date_from) throws ParseException {
		Company company = companyRepository.findOne(company_id);
		List<DutyInstance> result = new ArrayList<DutyInstance>();

		Date dateTo = new SimpleDateFormat("y-M-d").parse(date_to);
		Date dateFrom = new SimpleDateFormat("y-M-d").parse(date_from);

		List<DutyClause> clauses = clauseRepository.getRelated(dateFrom, dateTo);

		result = company.getDuties().stream()
				.flatMap(duty -> duty.getOcurrencesInRange(dateFrom, dateTo).stream().map(occ -> {
					Calendar cal = Calendar.getInstance();
					cal.setTime(occ);

					Optional<DutyClause> clause = clauses.stream().filter(dc -> dc.getPrimaryDate().equals(cal))
							.findFirst();

					return new DutyInstance(duty, clause.isPresent() ? clause.get().getAlternativeDate() : cal,
							clause.isPresent() ? clause.get() : null);
				})).collect(Collectors.toList());

		return ResponseEntity.ok(result);
	}

	@PostMapping("/duties")
	public ResponseEntity<?> createCompanyDuties(@PathVariable Long company_id, @Valid @RequestBody Duty duty,
			BindingResult bindingResult) {
		Company company = companyRepository.findOne(company_id);

		if (bindingResult.hasErrors())
			throw new InvalidRequestException("Invalid data", bindingResult);

		duty.setCompany(company);

		return ResponseEntity.ok(dutyRepository.save(duty));
	}

	@GetMapping("/notifications")
	public ResponseEntity<?> getNotifications(@PathVariable Long company_id) {
		Company company = companyRepository.findOne(company_id);
		return ResponseEntity.ok(company.getNotifications());
	}

	@GetMapping("/attributes")
	public ResponseEntity<?> getAttributes(@PathVariable Long company_id) {
		Company company = companyRepository.findOne(company_id);
		return ResponseEntity.ok(company.getAttributes());
	}

	@PostMapping("/attributes")
	public ResponseEntity<?> postAttribute(@PathVariable Long company_id, @RequestBody Attribute attr) {
		Company company = companyRepository.findOne(company_id);
		attr.setCompany(company);
		return ResponseEntity.ok(attributeRepository.save(attr));
	}
}
