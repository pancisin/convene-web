package com.pancisin.employger.controllers;

import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

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
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.pancisin.employger.models.Company;
import com.pancisin.employger.models.Customer;
import com.pancisin.employger.models.Duty;
import com.pancisin.employger.repository.CustomerRepository;
import com.pancisin.employger.repository.DutyRepository;

@Controller
@RequestMapping("/report")
public class ReportController {

	@Autowired
	private DutyRepository dutyRepository;

	@Autowired 
	private CustomerRepository customerRepository;
	
	@GetMapping("/customer/{customer_id}")
	public void getCustomerReport(@PathVariable Long customer_id, HttpServletResponse response,
			OutputStream outputStream) throws DocumentException {
		Customer customer = customerRepository.findOne(customer_id);
		
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=file.pdf");
		
		Document document = createDocument(customer.getCompany(), outputStream);
		document.addTitle("test");
		
		PdfPTable table = new PdfPTable(4);
		table.addCell("Date");
		table.addCell("Location");
		table.addCell("Description");
		table.addCell("Employees");

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.WEEK_OF_MONTH, 1);

		
		for (Duty duty : customer.getDuties()) {
			table.addCell(duty.getNextOcurrences(1, new Date()).toString());
			table.addCell(duty.getLocation());
			table.addCell(duty.getDescription());
			table.addCell(duty.getEmployees().toString());
		}
		
//		for (Date date : duty.getOcurrencesInRange(new Date(), c.getTime())) {
//			table.addCell(date.toString());
//			table.addCell(duty.getLocation());
//			table.addCell(duty.getDescription());
//			table.addCell(duty.getEmployees().toString());
//		}

		document.open();
		document.add(table);
		document.close();
	}

	@GetMapping("/duty/{duty_id}")
	public void something(@PathVariable Long duty_id, HttpServletResponse response, OutputStream outputStream)
			throws DocumentException {
		Duty duty = dutyRepository.findOne(duty_id);

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=file.pdf");

		Document document = createDocument(duty.getCompany(), outputStream);
		document.addTitle("test");

		PdfPTable table = new PdfPTable(4);
		table.addCell("Date");
		table.addCell("Location");
		table.addCell("Description");
		table.addCell("Employees");

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

		document.add(new Paragraph(" "));
		document.add(new Paragraph(" "));
		document.add(new Paragraph(" "));

		Paragraph header = new Paragraph(company.getName());
		header.setAlignment(Element.ALIGN_CENTER);

		document.add(header);
		return document;
	}
}
