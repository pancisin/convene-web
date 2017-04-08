package com.pancisin.employger.controllers;

import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.pancisin.employger.models.Company;
import com.pancisin.employger.models.Duty;
import com.pancisin.employger.repository.DutyRepository;

@Controller
public class ReportController {

	@Autowired
	private DutyRepository dutyRepository;

	@GetMapping("/report/duty/{duty_id}")
	public void something(@PathVariable Long duty_id, HttpServletResponse response, OutputStream outputStream)
			throws DocumentException {

		 Duty duty = dutyRepository.findOne(duty_id);

//		List<Duty> duties = dutyRepository.findAll();

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=file.pdf");

		Document document = new Document();
		PdfWriter.getInstance(document, outputStream);
		document.open();

		document.addTitle(duty.getLocation());
		document.addSubject("Bank Statement");
		document.addKeywords("Report, PDF, iText");
		document.addAuthor("Satyam");
		document.addCreator("Satyam");

		document.add(new Paragraph(" "));
		document.add(new Paragraph(" "));
		document.add(new Paragraph(" "));

		Company company = duty.getCompany();
		Paragraph header = new Paragraph(company.getName());
		header.setAlignment(Element.ALIGN_CENTER);
		
		document.add(header);
		
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
}
