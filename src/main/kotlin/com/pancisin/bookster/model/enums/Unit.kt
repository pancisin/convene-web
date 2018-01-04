package com.pancisin.bookster.model.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Unit private constructor(
  @JsonProperty("name")
  val prop: String,
  val code: String
) {
  HOUR("HOUR", "unit.hour"), MINUTE("MINUTE", "unit.minute"), SQUARED_METER("SQUARED_METER",
    "unit.squared_meter"),
  PIECE("PIECE", "unit.piece")
}
