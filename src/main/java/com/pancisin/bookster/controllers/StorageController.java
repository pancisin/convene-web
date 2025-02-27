package com.pancisin.bookster.controllers;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import com.pancisin.bookster.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pancisin.bookster.components.storage.StorageService;

@Controller
public class StorageController {

	@Autowired
	private StorageService storageService;

	@GetMapping("/files/**/{filename:.+}")
	@CrossOrigin
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename, HttpServletRequest request) throws ResourceNotFoundException {
		String url = request.getRequestURL().toString();
		Resource file = storageService.loadAsResource(url.split("/files/")[1]);

		if (file != null) {
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
					.cacheControl(CacheControl.maxAge(7, TimeUnit.DAYS)).body(file);
		} else {
			throw new ResourceNotFoundException("Resource not found");
		}
	}
}
