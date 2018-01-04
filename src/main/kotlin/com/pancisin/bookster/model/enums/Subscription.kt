package com.pancisin.bookster.model.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Subscription private constructor(
  @JsonProperty("name")
  val prop: String,
  val code: String,
  val price: Int,
  val eventLimit: Int,
  val pageLimit: Int,
  val serviceLimit: Int,
  val conferenceLimit: Int
) {
  FREE("FREE", "subscription.free", 0, 2, 0, 0, 0), STARTER("STARTER", "subscription.starter", 9, 20, 1, 3,
    0),
  PROFESSIONAL("PROFESSIONAL", "subscription.professional", 29, 100, 10, 100,
    0),
  ENTERPRISE("ENTERPRISE", "subscription.enterprise", 89, 100, 100, 100, 100)
}
