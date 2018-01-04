package com.pancisin.bookster.model.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class SubscriptionState private constructor(
  @JsonProperty("name")
  val prop: String,
  val code: String
) {
  NEW("NEW", "subscription.state.new"), PAID("PAID", "subscription.state.paid"), ACTIVE("ACTIVE",
    "subscription.state.active"),
  EXPIRED("EXPIRED",
    "subscription.state.expired"),
  UNPAID("UNPAID", "subscription.state.unpaid")
}
