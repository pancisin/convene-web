package com.pancisin.bookster.rest.controllers;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.model.MetaField;
import com.pancisin.bookster.model.MetaValue;
import com.pancisin.bookster.model.Survey;
import com.pancisin.bookster.model.SurveySubmission;
import com.pancisin.bookster.models.User;
import com.pancisin.bookster.model.enums.SurveyState;
import com.pancisin.bookster.repository.MetaFieldRepository;
import com.pancisin.bookster.repository.SurveyRepository;
import com.pancisin.bookster.repository.SurveySubmissionRepository;

@RestController
@RequestMapping("/api/survey/{survey_id}")
public class SurveyController {

	@Autowired
	private SurveyRepository surveyRepository;

	@Autowired
	private MetaFieldRepository mfRepository;

	@Autowired
	private SurveySubmissionRepository ssRepository;

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
		stored.setMetaFields(survey.getMetaFields());

		return ResponseEntity.ok(surveyRepository.save(stored));
	}

	@DeleteMapping
	public ResponseEntity<?> deleteSurvey(@PathVariable Long survey_id) {
		Survey stored = surveyRepository.findOne(survey_id);
		stored.setState(SurveyState.DELETED);
		return ResponseEntity.ok(surveyRepository.save(stored));
	}

	@PostMapping("/meta-fields")
	public ResponseEntity<?> postMetaFields(@PathVariable Long survey_id, @RequestBody List<MetaField> fields) {
		Survey stored = surveyRepository.findOne(survey_id);

		fields = mfRepository.save(fields);
		stored.setMetaFields(fields);

		return ResponseEntity.ok(fields);
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

	@PostMapping("/submission")
	public ResponseEntity<?> postSubmission(@PathVariable Long survey_id, @RequestBody List<MetaValue> values) {
		Survey stored = surveyRepository.findOne(survey_id);
		User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		SurveySubmission submission = new SurveySubmission(auth, stored);
		submission.setValues(values);

		try {
			submission = ssRepository.save(submission);
		} catch (Exception ex) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		return ResponseEntity.ok(submission);
	}

	@GetMapping("/submission")
	public ResponseEntity<?> getSubmissions(@PathVariable Long survey_id) {
		Survey stored = surveyRepository.findOne(survey_id);
		return ResponseEntity.ok(stored.getSubmissions());
	}

	@PatchMapping("/toggle-published")
	public ResponseEntity<?> togglePublished(@PathVariable Long survey_id) {
		Survey stored = surveyRepository.findOne(survey_id);

		if (stored.getState() == SurveyState.NEW) {
			stored.setState(SurveyState.IN_PROGRESS);
		} else if (stored.getState() == SurveyState.IN_PROGRESS) {
			stored.setState(SurveyState.NEW);
		}

		return ResponseEntity.ok(surveyRepository.save(stored));
	}
}
