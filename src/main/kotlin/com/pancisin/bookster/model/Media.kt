package com.pancisin.bookster.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonView
import com.pancisin.bookster.model.dtos.UserDto
import com.pancisin.bookster.models.views.Summary
import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity @Table(name = "medias")
data class Media(

  @Id @JsonView(Summary::class) @GeneratedValue(generator = "uuid2") @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(updatable = false, nullable = false)
  val id: UUID? = null,

  @Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
  val created: Calendar? = null,

  @Column @JsonView(Summary::class)
  var title: String? = null,

  @Column
  var description: String? = null,

  @Column @JsonView(Summary::class)
  var path: String? = null,

  @Column
  var deleted: Boolean = false,

  @Transient @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  var data: String? = null,

  @JsonIgnore
  @ManyToOne(optional = true)
  var author: User? = null,

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  var size: Long? = null
) {
  constructor(path: String) : this(null, null, null, null, path, false, null, null, null)
}
