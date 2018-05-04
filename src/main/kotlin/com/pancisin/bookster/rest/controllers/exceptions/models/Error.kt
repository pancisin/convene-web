package com.pancisin.bookster.rest.controllers.exceptions.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Error(
  var code: String,
  var message: String
) {
  var fieldErrors: List<FieldError> = ArrayList()
}
