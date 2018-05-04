package com.pancisin.bookster.rest.controllers.exceptions

import com.pancisin.bookster.rest.controllers.exceptions.models.Error
import com.pancisin.bookster.rest.controllers.exceptions.models.FieldError
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class RequestExceptionHandler : ResponseEntityExceptionHandler() {

  @ExceptionHandler(InvalidRequestException::class)
  fun handleInvalidRequest(e: RuntimeException, request: WebRequest): ResponseEntity<Any> {
    val ire = e as InvalidRequestException
    val error = Error("InvalidRequest", ire.message.toString())
    error.fieldErrors = ire.errors.fieldErrors.map {
      FieldError(
        resource = it.objectName,
        field = it.field,
        code = it.code,
        message = it.defaultMessage
      )
    }

    val headers = HttpHeaders().apply {
      contentType = MediaType.APPLICATION_JSON
    }

    return handleExceptionInternal(e, error, headers, HttpStatus.UNPROCESSABLE_ENTITY, request)
  }

  @ExceptionHandler(UnallowedRequestException::class)
  fun handleUnallowedRequest(e: RuntimeException, request: WebRequest) : ResponseEntity<Any> {
    val ure = e as UnallowedRequestException
    val error = Error("error.unallowed_data_access", ure.message.toString())
    val headers = HttpHeaders().apply {
      contentType = MediaType.APPLICATION_JSON
    }

    return handleExceptionInternal(e, error, headers, HttpStatus.FORBIDDEN, request)
  }
}
