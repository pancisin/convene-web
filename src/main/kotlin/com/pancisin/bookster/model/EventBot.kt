package com.pancisin.bookster.model

import java.util.Calendar
import java.util.UUID

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

import org.hibernate.annotations.GenericGenerator

import com.fasterxml.jackson.annotation.JsonIgnore
import com.pancisin.bookster.model.interfaces.IBot

@Entity
@Table(name = "eventBots")
data class EventBot(

  @Id @GeneratedValue(generator = "uuid2") @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
  val id: UUID? = null,

  @Column
  var fbPageId: String? = null,

  @JsonIgnore @ManyToOne
  var page: Page? = null,

  @Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
  private val created: Calendar? = null,

  @Column
  override var name: String? = null,

  @Column
  override var active: Boolean = false,

  @JsonIgnore
  @OneToMany(mappedBy = "eventBot", orphanRemoval = true)
  override var runs: List<BotRun> = ArrayList()
) : IBot
