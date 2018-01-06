package com.pancisin.bookster.rest.controllers.v1

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

import com.pancisin.bookster.model.MetaField
import com.pancisin.bookster.model.MetaValue
import com.pancisin.bookster.model.Survey
import com.pancisin.bookster.model.SurveySubmission
import com.pancisin.bookster.model.User
import com.pancisin.bookster.model.enums.SurveyState
import com.pancisin.bookster.repository.MetaFieldRepository
import com.pancisin.bookster.repository.SurveyRepository
import com.pancisin.bookster.repository.SurveySubmissionRepository

@RestController
@RequestMapping("/api/v1/survey/{survey_id}")
class SurveyController {

  @Autowired
  lateinit var surveyRepository: SurveyRepository

  @Autowired
  lateinit var mfRepository: MetaFieldRepository

  @Autowired
  lateinit var ssRepository: SurveySubmissionRepository

  @GetMapping
  fun getSurvey(@PathVariable survey_id: Long?) = ResponseEntity.ok(surveyRepository.findOne(survey_id))

  @PutMapping
  fun putSurvey(@PathVariable survey_id: Long?, @RequestBody survey: Survey): ResponseEntity<Survey> {
    val stored = surveyRepository.findOne(survey_id).apply {
      name = survey.name;
      start_date = survey.start_date;
      end_date = survey.end_date;
      metaFields = survey.metaFields
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

  @PostMapping("/meta-fields")
  fun postMetaFields(@PathVariable survey_id: Long?, @RequestBody fields: MutableList<MetaField>): ResponseEntity<List<MetaField>> {
    val stored = surveyRepository.findOne(survey_id)
    var fields = fields

    fields = mfRepository.save(fields)
    stored.metaFields = fields

    return ResponseEntity.ok(fields)
  }

  @Transactional
  @PostMapping("/meta-field")
  fun postMetaField(@PathVariable survey_id: Long?, @RequestBody field: MetaField): ResponseEntity<MetaField> {
    var field = field
    val stored = surveyRepository.findOne(survey_id)

    field = mfRepository.save(field)
    stored.metaFields.add(field)
    surveyRepository.save(stored)

    return ResponseEntity.ok(field)
  }

  @GetMapping("/meta-field")
  fun getMetaFields(@PathVariable survey_id: Long?) = ResponseEntity.ok( surveyRepository.findOne(survey_id))

  @PostMapping("/submission")
  fun postSubmission(@PathVariable survey_id: Long?, @RequestBody values: MutableList<MetaValue>): ResponseEntity<SurveySubmission> {
    val stored = surveyRepository.findOne(survey_id)
    val auth = SecurityContextHolder.getContext().authentication.principal as User

    var submission = SurveySubmission(user =  auth, survey =  stored, values = values)

    try {
      submission = ssRepository.save(submission)
    } catch (ex: Exception) {
      return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    return ResponseEntity.ok(submission)
  }

  @GetMapping("/submission")
  fun getSubmissions(@PathVariable survey_id: Long?) = ResponseEntity.ok(surveyRepository.findOne(survey_id)?.submissions)

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
