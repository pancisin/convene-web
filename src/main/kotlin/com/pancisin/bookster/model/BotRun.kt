package com.pancisin.bookster.model

import java.util.Calendar
import java.util.UUID

import org.hibernate.annotations.GenericGenerator

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.Access
import com.pancisin.bookster.model.enums.BotRunState
import com.pancisin.bookster.model.interfaces.IBot
import javax.persistence.*

@Entity
@Table(name = "bot_runs")
data class BotRun(

  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
  val id: UUID? = null,

  @Column(name = "date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
  var date: Calendar? = null,

  @Column
  @JsonProperty(access = Access.READ_ONLY)
  @Enumerated(EnumType.STRING)
  var state: BotRunState = BotRunState.SCHEDULED,

  @Column
  var dataCount: Int = 0
) {

  @JsonIgnoreProperties("fbPageId", "active", "ceated", "lastRun", "runsCount")
  @ManyToOne(optional = true)
  @JoinTable(
    name = "event_bots_runs",
    joinColumns = arrayOf(JoinColumn(name = "bot_run_id")),
    inverseJoinColumns = arrayOf(JoinColumn(name = "event_bot_id"))
  )
  var eventBot: EventBot? = null

  @JsonIgnoreProperties("active", "created", "lastRun", "runsCount", "parser", "sourceUrl", "sourceType", "name", "articlesList")
  @ManyToOne(optional = true)
  @JoinTable(
    name = "article_bots_runs",
    joinColumns = arrayOf(JoinColumn(name = "bot_run_id")),
    inverseJoinColumns = arrayOf(JoinColumn(name = "article_bot_id"))
  )
  var articleBot: ArticleBot? = null

  constructor(bot: EventBot, state: BotRunState): this() {
    this.eventBot = bot
    this.state = state
  }

  constructor(bot: ArticleBot, state: BotRunState): this() {
    this.articleBot = bot
    this.state = state
  }

  val bot: IBot?
    @Transient
    @JsonProperty("bot")
    @JsonIgnoreProperties("active", "created", "lastRun", "runsCount", "parser", "sourceUrl", "sourceType", "name", "articlesList")
    get () = if (eventBot != null) eventBot else articleBot

  @PrePersist
  private fun onCreate() {
    this.date = Calendar.getInstance()
  }
}
