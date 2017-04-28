package com.pancisin.employger.export;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.pancisin.employger.models.Company;

public class CompanyPageHeaderEvent extends PdfPageEventHelper {

	private Company company;

	public CompanyPageHeaderEvent(Company company) {
		this.company = company;
	}

	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		PdfContentByte cb = writer.getDirectContent();

		StringBuilder htext = new StringBuilder();
		htext.append(company.getName()).append(", ").append(company.getAddress().toString()).append(", ")
				.append(company.getEmail());

		Phrase header = new Phrase(htext.toString());
		header.add(new Paragraph(" "));
		header.add(new Paragraph(" "));

		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, header,
				(document.right() - document.left()) / 2 + document.leftMargin(), document.top() + 10, 0);

	}
}
