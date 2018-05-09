package com.pancisin.bookster.rest.controllers.exceptions.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class Error(
  var code: Int,
  var type: ErrorType,
  var message: String
) {
  var errors: List<FieldError> = ArrayList()
  val timestamp = Calendar.getInstance().timeInMillis
}
