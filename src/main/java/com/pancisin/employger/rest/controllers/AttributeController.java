package com.pancisin.employger.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.employger.models.Attribute;
import com.pancisin.employger.models.AttributeOption;
import com.pancisin.employger.repository.AttributeOptionRepository;
import com.pancisin.employger.repository.AttributeRepository;

@RestController
@PreAuthorize("hasPermission(#attribute_id, 'attribute', '')")
@RequestMapping("/api/attribute/{attribute_id}")
public class AttributeController {

	@Autowired
	private AttributeRepository attributeRepository;

	@Autowired
	private AttributeOptionRepository attributeOptionRepository;

	@GetMapping
	public ResponseEntity<?> getAttribute(@PathVariable Long attribute_id) {
		return ResponseEntity.ok(attributeRepository.findOne(attribute_id));
	}

	@PutMapping
	public ResponseEntity<?> putAttribute(@PathVariable Long attribute_id) {
		return null;
	}

	@DeleteMapping
	public ResponseEntity<?> deleteAttribute(@PathVariable Long attribute_id) {
		attributeRepository.delete(attribute_id);
		return ResponseEntity.ok("success");
	}

	@GetMapping("/options")
	public ResponseEntity<?> getAttributeOptions(@PathVariable Long attribute_id) {
		return ResponseEntity.ok(attributeRepository.findOne(attribute_id).getOptions());
	}

	@PostMapping("/options")
	public ResponseEntity<?> postAttributeOption(@PathVariable Long attribute_id, @RequestBody AttributeOption option) {
		Attribute attr = attributeRepository.findOne(attribute_id);
		option.setAttribute(attr);
		return ResponseEntity.ok(attributeOptionRepository.save(option));
	}

	@DeleteMapping("/options/{option_id}")
	public ResponseEntity<?> deleteOption(@PathVariable Long attribute_id, @PathVariable Long option_id) {
		Attribute attr = attributeRepository.findOne(attribute_id);
		if (attr.getOptions().stream().anyMatch(o -> o.getId() == option_id))
			attributeOptionRepository.delete(option_id);
		return ResponseEntity.ok("success");
	}
}
