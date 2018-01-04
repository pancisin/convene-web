package com.pancisin.bookster.model

import java.util.UUID

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.Transient

import org.hibernate.annotations.GenericGenerator

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.pancisin.bookster.model.enums.BotRunState

@Entity
@Table(name = "pages_imports")
data class PageImport(
  @Id @GeneratedValue(generator = "uuid2") @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
  val id: UUID? = null,

  @Transient
  var state: BotRunState? = null,

  @OneToOne @JsonIgnoreProperties("gallery", "metadata", "followersCount")
  var page: Page? = null,

  @Column
  var sourceName: String? = null,

  @Column(unique = true)
  var sourceId: String? = null
) {
  constructor(state: BotRunState) : this(null, state)
  constructor(state: BotRunState, page: Page) : this(null, state, page)
}

