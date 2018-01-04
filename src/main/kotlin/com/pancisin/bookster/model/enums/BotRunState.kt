package com.pancisin.bookster.model.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class BotRunState private constructor(
  @JsonProperty("name")
  val prop: String,
  val code: String
) {
  SCHEDULED("SCHEDULED", "bot.state.scheduled"),
  RUNNING("RUNNING", "bot.state.running"),
  SUCCESS("SUCCESS", "bot.state.success"),
  ERROR("ERROR", "bot.state.error"),
  UNKNOWN("UNKNOWN", "bot.state.unknown")
}
