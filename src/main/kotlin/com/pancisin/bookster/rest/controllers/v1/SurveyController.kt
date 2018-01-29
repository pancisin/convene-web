package com.pancisin.bookster.rest.controllers.v1

import com.pancisin.bookster.model.*
import javax.transaction.Transactional

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import com.pancisin.bookster.model.enums.SurveyState
import com.pancisin.bookster.repository.SurveyRepository
import com.pancisin.bookster.repository.FormSubmissionRepository

@RestController
@RequestMapping("/api/v1/survey/{survey_id}")
class SurveyController {

  @Autowired
  lateinit var surveyRepository: SurveyRepository

  @GetMapping
  fun getSurvey(@PathVariable survey_id: Long?) = ResponseEntity.ok(surveyRepository.findOne(survey_id))

  @PutMapping
  fun putSurvey(@PathVariable survey_id: Long?, @RequestBody survey: Survey): ResponseEntity<Survey> {
    val stored = surveyRepository.findOne(survey_id).apply {
      name = survey.name
      start_date = survey.start_date
      end_date = survey.end_date
      form = survey.form
    }
    return ResponseEntity.ok(surveyRepository.save(stored))
  }

  @DeleteMapping
  fun deleteSurvey(@PathVariable survey_id: Long?): ResponseEntity<Survey> {
    val stored = surveyRepository.findOne(survey_id).apply {
      state = SurveyState.DELETED
    }
    return ResponseEntity.ok(surveyRepository.save(stored))
  }

  @PostMapping("/formField-fields")
  fun postMetaFields(@PathVariable survey_id: Long?, @RequestBody fields: MutableList<FormField>): ResponseEntity<List<FormField>> {
    val stored = surveyRepository.findOne(survey_id)
    stored.form.formFields = fields
    return ResponseEntity.ok(fields)
  }

  @GetMapping("/formField-field")
  fun getMetaFields(@PathVariable survey_id: Long?) = ResponseEntity.ok( surveyRepository.findOne(survey_id))

  @PatchMapping("/toggle-published")
  fun togglePublished(@PathVariable survey_id: Long?): ResponseEntity<Survey> {
    val stored = surveyRepository.findOne(survey_id)

    if (stored.state === SurveyState.NEW) {
      stored.state = SurveyState.IN_PROGRESS
    } else if (stored.state === SurveyState.IN_PROGRESS) {
      stored.state = SurveyState.NEW
    }

    return ResponseEntity.ok(surveyRepository.save(stored))
  }
}
