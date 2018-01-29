package com.pancisin.bookster.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonView
import com.pancisin.bookster.models.views.Summary
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "articles")
data class Article(

  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long? = null,

  @Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
  var created: Calendar? = null,

  @Column
  var date: Calendar? = null,

  @Column
  var title: String? = null,

  @Lob @Column
  var content: String? = null,

  @OneToOne(optional = true, cascade = arrayOf(CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH)) @JsonView(Summary::class)
  var thumbnail: Media? = null,

  @Transient @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  var thumbnailData: String? = null,

  @ManyToOne @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  var author: User? = null,

  @Column
  var published: Boolean = false,

  @JsonIgnore @ManyToOne(optional = true)
  @JoinTable(
    name = "pages_articles",
    joinColumns = arrayOf(JoinColumn(name = "articles_id")),
    inverseJoinColumns = arrayOf(JoinColumn(name = "page_id"))
  )
  var page: Page? = null,

  @Column(unique = true, updatable = false, nullable = false)
  var identifier: Int = 0,

  @JsonIgnore @ManyToOne(optional = true)
  @JoinTable(
    name = "articles_lists_articles",
    joinColumns = arrayOf(JoinColumn(name = "articles_id")),
    inverseJoinColumns = arrayOf(JoinColumn(name = "articles_list_id"))
  )
  var articlesList: ArticlesList? = null

) {
  @PrePersist
  private fun onCreate() {
    this.identifier = arrayListOf(
      title.toString(),
      articlesList?.id.toString() ?: page?.id.toString() ?: ""
    ).hashCode()
  }
}
