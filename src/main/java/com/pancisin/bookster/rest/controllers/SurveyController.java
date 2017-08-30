package com.pancisin.bookster.rest.controllers;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.models.MetaField;
import com.pancisin.bookster.models.Survey;
import com.pancisin.bookster.repository.MetaFieldRepository;
import com.pancisin.bookster.repository.SurveyRepository;

@RestController
@RequestMapping("/api/survey/{survey_id}")
public class SurveyController {

	@Autowired
	private SurveyRepository surveyRepository;

	@Autowired
	private MetaFieldRepository mfRepository;

	@GetMapping
	public ResponseEntity<?> getSurvey(@PathVariable Long survey_id) {
		return ResponseEntity.ok(surveyRepository.findOne(survey_id));
	}

	@PutMapping
	public ResponseEntity<?> putSurvey(@PathVariable Long survey_id, @RequestBody Survey survey) {
		Survey stored = surveyRepository.findOne(survey_id);

		stored.setName(survey.getName());
		stored.setStart_date(survey.getStart_date());
		stored.setEnd_date(survey.getEnd_date());

		return ResponseEntity.ok(surveyRepository.save(stored));
	}

	@DeleteMapping
	public ResponseEntity<?> deleteSurvey(@PathVariable Long survey_id) {
		return null;
	}

	@Transactional
	@PostMapping("/meta-field")
	public ResponseEntity<?> postMetaField(@PathVariable Long survey_id, @RequestBody MetaField field) {
		Survey stored = surveyRepository.findOne(survey_id);

		field = mfRepository.save(field);
		stored.addMetaField(field);
		surveyRepository.save(stored);

		return ResponseEntity.ok(field);
	}

	@GetMapping("/meta-field")
	public ResponseEntity<?> getMetaFields(@PathVariable Long survey_id) {
		Survey stored = surveyRepository.findOne(survey_id);
		return ResponseEntity.ok(stored.getMetaFields());
	}
}
