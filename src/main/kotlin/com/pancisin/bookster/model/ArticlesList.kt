package com.pancisin.bookster.model

import java.util.UUID

import javax.persistence.CollectionTable
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.OneToMany
import javax.persistence.PreUpdate
import javax.persistence.Table

import org.hibernate.annotations.GenericGenerator

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

@Entity
@Table(name = "articles_list")
data class ArticlesList(

  @Id @GeneratedValue(generator = "uuid2") @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(updatable = false, nullable = false)
  val id: UUID? = null,

  @JsonIgnore
  @ManyToMany(mappedBy = "articlesList")
  val articles: List<Article>? = null,

  @Column
  var name: String? = null,

  @JsonIgnore
  @OneToMany(mappedBy = "articlesList")
  private val bots: List<ArticleBot>? = null,

  @Column(unique = true, nullable = false)
  var tagsHash: Int = 0,

  @ElementCollection
  @CollectionTable(name = "articles_lists_tags")
  var tags: MutableList<String> = ArrayList()

) {
  @PreUpdate
  private fun onUpdate() {
    this.tagsHash = this.tags.sorted().hashCode()
  }
}
