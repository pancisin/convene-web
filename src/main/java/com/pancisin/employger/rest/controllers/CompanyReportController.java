package com.pancisin.employger.rest.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.employger.models.enums.ReportInterval;

@RestController
@RequestMapping("/api/report")
public class CompanyReportController {

	@GetMapping("/periods")
	public ResponseEntity<?> getPeriods() {
		return ResponseEntity.ok(ReportInterval.values());
	}
}
