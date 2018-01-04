package com.pancisin.bookster.model

import java.util.Calendar
import java.util.UUID

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.PrePersist
import javax.persistence.Table

import com.pancisin.bookster.model.ArticleBot
import org.hibernate.annotations.GenericGenerator

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.Access
import com.pancisin.bookster.models.enums.BotRunState

@Entity
@Table(name = "article_bots_runs")
data class ArticleBotRun(

  @Id @GeneratedValue(generator = "uuid2") @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
  val id: UUID? = null,

  @Column(name = "date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
  var date: Calendar? = null,

  @Column @JsonProperty(access = Access.READ_ONLY) @Enumerated(EnumType.STRING)
  var state: BotRunState = BotRunState.SCHEDULED,

  @Column
  var articlesCount: Int = 0,

  @JsonIgnoreProperties("active", "created", "lastRun", "runsCount", "parser", "sourceUrl", "sourceType", "name", "articlesList")
  @ManyToOne(optional = false)
  val bot: ArticleBot? = null
) {
  constructor(bot: ArticleBot, state: BotRunState) : this(null, null, state, 0, bot)

  @PrePersist
  private fun onCreate() {
    this.date = Calendar.getInstance()
  }
}
