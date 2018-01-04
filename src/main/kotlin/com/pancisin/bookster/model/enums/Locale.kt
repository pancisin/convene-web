package com.pancisin.bookster.model.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonView
import com.pancisin.bookster.models.views.Summary

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Locale private constructor(
  @JsonProperty("name")
  val prop: String,
  val code: String,
  val dateFormat: String,
  val timeFormat: String
) {
  sk("sk", "locale.sk", "DD.MM.YYYY", "HH:mm"), en("en", "locale.en", "MM/DD/YYYY", "H mm a")
}
