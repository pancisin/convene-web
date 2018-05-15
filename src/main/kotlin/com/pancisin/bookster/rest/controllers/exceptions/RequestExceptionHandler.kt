package com.pancisin.bookster.rest.controllers.exceptions

import com.pancisin.bookster.exceptions.InvalidRequestException
import com.pancisin.bookster.exceptions.ResourceLimitReachedException
import com.pancisin.bookster.exceptions.UnallowedRequestException
import com.pancisin.bookster.rest.controllers.exceptions.models.Error
import com.pancisin.bookster.rest.controllers.exceptions.models.ErrorType
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
    val error = Error(1401, ErrorType.INVALID_REQUEST, ire.message.toString())
    error.errors = ire.errors.fieldErrors.map {
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
  fun handleUnallowedRequest(e: RuntimeException, request: WebRequest): ResponseEntity<Any> {
    val ure = e as UnallowedRequestException
    val error = Error(1402, ErrorType.UNALLOWED_RESOURCE_ACCESS, ure.message.toString())
    val headers = HttpHeaders().apply {
      contentType = MediaType.APPLICATION_JSON
    }

    return handleExceptionInternal(e, error, headers, HttpStatus.FORBIDDEN, request)
  }

  @ExceptionHandler(ResourceLimitReachedException::class)
  fun handleResourceLimitReached(e: RuntimeException, request: WebRequest): ResponseEntity<Any> {
    val rlre = e as ResourceLimitReachedException
    val error = Error(1403, ErrorType.UNALLOWED_RESOURCE_ACCESS, rlre.message.toString())
    val headers = HttpHeaders().apply {
      contentType = MediaType.APPLICATION_JSON
    }

    return handleExceptionInternal(e, error, headers, HttpStatus.PAYMENT_REQUIRED, request)
  }

//  @ExceptionHandler(BadHttpRequest::class)
//  fun handleBadRequest(e: RuntimeException, request: WebRequest) : ResponseEntity<Any> {
//    val bre = e as BadHttpRequest
//
//    val error = Error(1404, ErrorType.BAD_REQUEST, bre.message.toString())
//    val headers = HttpHeaders().apply {
//      contentType = MediaType.APPLICATION_JSON
//    }
//
//    return handleExceptionInternal(e, error, headers, HttpStatus.BAD_REQUEST, request)
//  }
}
