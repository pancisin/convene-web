package com.pancisin.bookster.rest.controllers.v1

import com.pancisin.bookster.model.Form
import com.pancisin.bookster.model.FormFieldValue
import com.pancisin.bookster.model.FormSubmission
import com.pancisin.bookster.model.User
import com.pancisin.bookster.repository.FormRepository
import com.pancisin.bookster.repository.FormSubmissionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
//@PreAuthorize("hasRole('SUPERADMIN')")
@RequestMapping("/api/v1/form/{form_id}")
class FormController {

  @Autowired
  lateinit var formRepository: FormRepository;

  @Autowired
  lateinit var formSubmissionRepository: FormSubmissionRepository;

  @GetMapping
  fun getForm(@PathVariable("form_id") form_id: UUID) = ResponseEntity.ok(formRepository.findOne(form_id))

  @PutMapping
  fun putForm(@PathVariable form_id: UUID, @RequestBody form: Form): ResponseEntity<Form> {
    val stored = formRepository.findOne(form_id).apply {
      formFields = form.formFields
    }

    return ResponseEntity.ok(formRepository.save(stored));
  }

  @PostMapping("/submission")
  fun postSubmission(@PathVariable form_id: UUID, @RequestBody values: MutableList<FormFieldValue>): ResponseEntity<FormSubmission> {
    val stored = formRepository.findOne(form_id);
    val auth = SecurityContextHolder.getContext().authentication.principal as User

    var submission = FormSubmission(user =  auth, form =  stored, values = values)

    try {
      submission = formSubmissionRepository.save(submission)
    } catch (ex: Exception) {
      return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    return ResponseEntity.ok(submission)
  }

  @GetMapping("/submission")
  fun getSubmissions(@PathVariable form_id: UUID) = ResponseEntity.ok(formRepository.findOne(form_id).submissions)
}
