package com.pancisin.bookster.rest.controllers.v1

import com.pancisin.bookster.model.SurveySubmission
import com.pancisin.bookster.repository.SurveySubmissionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import retrofit2.http.Path

@RestController
@RequestMapping("/api/v1/survey-submission/{survey_submission_id}")
class SurveySubmissionController {

  @Autowired
  lateinit var surveySubmissionRepository: SurveySubmissionRepository

  @GetMapping
  fun getSurveySubmission(@PathVariable survey_submission_id: Long) = surveySubmissionRepository.findOne(survey_submission_id)

  @DeleteMapping
  fun deleteSurveySubmission(@PathVariable survey_submission_id: Long) = surveySubmissionRepository.delete(survey_submission_id)

  @PutMapping
  fun putSurveySubmission(@PathVariable survey_submission_id: Long, @RequestBody submission: SurveySubmission) : ResponseEntity<SurveySubmission> {
    val stored = surveySubmissionRepository.findOne(survey_submission_id).apply {
      values = submission.values
    }
    return ResponseEntity.ok(surveySubmissionRepository.save(submission))
  }

}
