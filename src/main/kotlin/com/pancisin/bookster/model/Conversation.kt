package com.pancisin.bookster.model

import java.util.ArrayList
import java.util.Comparator

data class Conversation(
  var participant: User? = null,
  var recentMessages: MutableList<Message> = ArrayList<Message>()
) {
  constructor(participant: User, message: Message) : this(participant) {
    recentMessages.add(message)
  }

  val lastContact: Long?
    get() {
      return recentMessages.map { m -> m.created!!.timeInMillis }.maxWith(Comparator.comparingLong{ x -> x})
    }
}
