package com.pancisin.bookster.rest.controllers.v1

import com.pancisin.bookster.model.FormSubmission
import com.pancisin.bookster.repository.FormSubmissionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/form-submission/{form_submission_id}")
class SurveySubmissionController {

  @Autowired
  lateinit var formSubmissionRepository: FormSubmissionRepository

  @GetMapping
  fun getSurveySubmission(@PathVariable form_submission_id: Long) = formSubmissionRepository.findOne(form_submission_id)

  @DeleteMapping
  fun deleteSurveySubmission(@PathVariable form_submission_id: Long) = formSubmissionRepository.delete(form_submission_id)

  @PutMapping
  fun putSurveySubmission(@PathVariable form_submission_id: Long, @RequestBody submission: FormSubmission) : ResponseEntity<FormSubmission> {
    val stored = formSubmissionRepository.findOne(form_submission_id).apply {
      values = submission.values
    }
    return ResponseEntity.ok(formSubmissionRepository.save(submission))
  }

}
