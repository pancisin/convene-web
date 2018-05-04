package com.pancisin.bookster.rest.controllers.exceptions.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class FieldError(
  var resource: String,
  var field: String,
  var code: String,
  var message: String
)
