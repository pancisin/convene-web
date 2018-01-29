package com.pancisin.bookster.rest.controllers.v1

import com.pancisin.bookster.model.FormSubmission
import com.pancisin.bookster.repository.FormSubmissionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/survey-submission/{survey_submission_id}")
class SurveySubmissionController {

  @Autowired
  lateinit var surveySubmissionRepository: FormSubmissionRepository

  @GetMapping
  fun getSurveySubmission(@PathVariable survey_submission_id: Long) = surveySubmissionRepository.findOne(survey_submission_id)

  @DeleteMapping
  fun deleteSurveySubmission(@PathVariable survey_submission_id: Long) = surveySubmissionRepository.delete(survey_submission_id)

  @PutMapping
  fun putSurveySubmission(@PathVariable survey_submission_id: Long, @RequestBody submission: FormSubmission) : ResponseEntity<FormSubmission> {
    val stored = surveySubmissionRepository.findOne(survey_submission_id).apply {
      values = submission.values
    }
    return ResponseEntity.ok(surveySubmissionRepository.save(submission))
  }

}
