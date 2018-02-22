package com.pancisin.bookster.model.enums

import com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.STRING)
enum class RecipientType {
  USER,
  PAGE,
  EVENT
}
