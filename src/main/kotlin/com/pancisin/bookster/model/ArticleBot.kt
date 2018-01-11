package com.pancisin.bookster.model

import java.util.Calendar
import java.util.HashMap
import java.util.UUID

import javax.persistence.CollectionTable
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.MapKeyColumn
import javax.persistence.OneToMany
import javax.persistence.Table

import org.hibernate.annotations.GenericGenerator

import com.fasterxml.jackson.annotation.JsonIgnore
import com.pancisin.bookster.model.enums.BotSourceType
import com.pancisin.bookster.model.interfaces.IBot

@Entity
@Table(name = "article_bots")
data class ArticleBot(

  @Id @GeneratedValue(generator = "uuid2") @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
  val id: UUID? = null,

  @ElementCollection(fetch = FetchType.EAGER) @MapKeyColumn(name = "name") @Column(name = "value")
  @CollectionTable(name = "article_bots_parsers", joinColumns = arrayOf(JoinColumn(name = "article_bot_id")))
  var parser: MutableMap<String, String> = HashMap(),

  @JsonIgnore @ManyToOne(optional = false)
  var articlesList: ArticlesList? = null,

  @Column
  var sourceUrl: String? = null,

  @Enumerated(EnumType.STRING)
  var sourceType: BotSourceType = BotSourceType.REST_API,

  @Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
  val created: Calendar? = null,

  @Column
  override var name: String? = null,

  @Column
  override var active: Boolean = false,

  @JsonIgnore
  @OneToMany(mappedBy = "articleBot", orphanRemoval = true)
  override var runs: List<BotRun> = ArrayList()
) : IBot
