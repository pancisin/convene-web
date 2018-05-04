package com.pancisin.bookster.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "ratings", uniqueConstraints = arrayOf(UniqueConstraint(columnNames = arrayOf("event_id", "user_id"))))
data class Rating(

  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
  var id: UUID? = UUID.randomUUID(),

  @Column
  var starsCount: Int = 0,

  @Column
  var summary: String = "",

  @ManyToOne
  @JsonIgnore
  var event: Event? = null,

  @ManyToOne
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  var user: User? = null
)
