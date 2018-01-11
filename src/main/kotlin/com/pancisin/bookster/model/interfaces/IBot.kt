package com.pancisin.bookster.model.interfaces

import com.pancisin.bookster.model.BotRun
import javax.persistence.Column
import javax.persistence.Transient

interface IBot {
  var name: String?
  var active: Boolean
  var runs: List<BotRun>

  val runsCount: Int
    @Transient
    get() = runs.size

  val lastRun: BotRun?
    @Transient
    get() = runs.maxBy { r -> r.date!!.timeInMillis }
}
