package com.pancisin.bookster.rest.controllers.exceptions.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Error(
  var code: Int,
  var type: ErrorType,
  var message: String
) {
  var errors: List<FieldError> = ArrayList()
}
