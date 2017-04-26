package com.pancisin.employger.controllers;

import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.pancisin.employger.models.Attribute;
import com.pancisin.employger.models.AttributeOption;
import com.pancisin.employger.models.Company;
import com.pancisin.employger.models.Customer;
import com.pancisin.employger.models.Duty;
import com.pancisin.employger.models.Task;
import com.pancisin.employger.repository.CustomerRepository;
import com.pancisin.employger.repository.DutyRepository;
import com.pancisin.employger.rest.controllers.objects.DutyInstance;

@Controller
@RequestMapping("/report")
public class ReportController {

	@Autowired
	private DutyRepository dutyRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@GetMapping("/customer/{customer_id}")
	public void getCustomerReport(@PathVariable Long customer_id, HttpServletResponse response,
			OutputStream outputStream) throws DocumentException, ParseException {
		Customer customer = customerRepository.findOne(customer_id);

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=file.pdf");

		Document document = createDocument(customer.getCompany(), outputStream);
		document.setMargins(0, 0, 25, 25);
		document.addTitle(customer.getName());

		List<Attribute> attributes = customer.getCompany().getAttributes();
		PdfPTable table = new PdfPTable(1 + attributes.size());
		table.addCell("Date");

		for (int i = 0; i < attributes.size(); i++) {
			table.addCell(attributes.get(i).getName());
		}

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, 1);

		List<DutyInstance> instances = customer.getDutyInstances(new Date(), c.getTime());
		instances = instances.stream().sorted((i1, i2) -> i1.getDate().compareTo(i2.getDate()))
				.collect(Collectors.toList());

		for (int i = 0; i < instances.size(); i++) {
			DutyInstance inst = instances.get(i);

			SimpleDateFormat format = new SimpleDateFormat("EEEE, d. MMMM y");
			PdfPCell dateCell = new PdfPCell(new Phrase(format.format(inst.getDate().getTime())));

			List<Task> tasks = inst.getDuty().getTasks();
			dateCell.setRowspan(tasks.size());
			dateCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			dateCell.setHorizontalAlignment(Element.ALIGN_CENTER);

			table.addCell(dateCell);

			for (int row = 0; row < tasks.size(); row++) {
				for (int col = 0; col < attributes.size(); col++) {
					final int current = col;
					Optional<com.pancisin.employger.rest.controllers.objects.Attribute> attr = tasks.get(row)
							.getAttributes().stream().filter(x -> x.getId() == attributes.get(current).getId())
							.findFirst();

					if (attr.isPresent()) {
						PdfPCell cell = new PdfPCell(new Phrase(attr.get().getValues().stream()
								.map(AttributeOption::toString).collect(Collectors.joining(", "))));
						cell.setPadding(5);
						table.addCell(cell);
					} else
						table.addCell("");
				}
			}
		}

		document.open();
		document.add(getHeader(customer.getCompany()));
		document.add(table);
		document.close();
	}

	@GetMapping("/duty/{duty_id}")
	public void something(@PathVariable Long duty_id, HttpServletResponse response, OutputStream outputStream)
			throws DocumentException, ParseException {
		Duty duty = dutyRepository.findOne(duty_id);

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=file.pdf");

		Document document = createDocument(duty.getCompany(), outputStream);
		document.addTitle(duty.getCustomer().getName());

		List<Attribute> attributes = duty.getCompany().getAttributes();

		PdfPTable table = new PdfPTable(1 + attributes.size());
		table.addCell("Date");

		for (int i = 0; i < attributes.size(); i++) {
			table.addCell(attributes.get(i).getName());
		}

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.WEEK_OF_MONTH, 1);

		for (Date date : duty.getOcurrencesInRange(new Date(), c.getTime())) {
			table.addCell(date.toString());
			table.addCell(duty.getLocation());
			table.addCell(duty.getDescription());
			table.addCell(duty.getEmployees().toString());
		}

		document.add(table);
		document.close();
	}

	private Document createDocument(Company company, OutputStream outputStream) throws DocumentException {
		Document document = new Document();
		PdfWriter.getInstance(document, outputStream);
		document.open();

		document.addSubject("Bank Statement");
		document.addKeywords("Report, PDF, iText");
		document.addAuthor(company.getName());
		document.addCreator(company.getName());

		return document;
	}

	private Paragraph getHeader(Company company) {
		StringBuilder htext = new StringBuilder();
		htext.append(company.getName()).append(", ").append(company.getAddress().toString()).append(", ")
				.append(company.getEmail());

		Paragraph header = new Paragraph(htext.toString());
		header.setAlignment(Element.ALIGN_CENTER);

		header.add(new Paragraph(" "));
		header.add(new Paragraph(" "));

		return header;
	}
}
