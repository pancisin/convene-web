package com.pancisin.bookster.model.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonView
import com.pancisin.bookster.models.views.Compact

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class PageRole private constructor(
  @JsonProperty("name")
  val prop: String,
  val code: String,
  val level: Int
) {
  ROLE_OWNER("ROLE_OWNER", "role.owner", 100),
  ROLE_ADMINISTRATOR("ROLE_ADMINISTRATOR", "role.administrator", 60),
  ROLE_MODERATOR("ROLE_MODERATOR", "role.moderator", 40)
}
