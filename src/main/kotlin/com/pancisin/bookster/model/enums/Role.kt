package com.pancisin.bookster.model.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonView
import com.pancisin.bookster.models.views.Compact

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Role private constructor(
  @JsonProperty("name")
  val prop: String,
  val code: String,
  val level: Int
) {
  ROLE_SUPERADMIN("ROLE_SUPERADMIN", "role.superadmin", 100),
  ROLE_ADMINISTRATOR("ROLE_ADMINISTRATOR", "role.administrator", 80),
  ROLE_SUPPORT("ROLE_SUPPORT", "role.support", 60),
  ROLE_AUTHOR("ROLE_AUTHOR", "role.author", 40),
  ROLE_VISITOR("ROLE_VISITOR", "role.visitor", 20)
}
