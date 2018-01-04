package com.pancisin.bookster.model

import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Id
import javax.persistence.GeneratedValue
import org.hibernate.annotations.GenericGenerator
import javax.persistence.Column
import java.util.UUID
import javax.persistence.ManyToOne
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import javax.persistence.Enumerated
import com.pancisin.bookster.model.enums.ActivityType
import java.util.Calendar
import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.JoinTable
import javax.persistence.JoinColumn
import javax.persistence.EnumType

@Entity
@Table(name = "activities")
data class Activity(

  @Id @GeneratedValue(generator = "uuid2") @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
  var id: UUID? = UUID.randomUUID(),

  @ManyToOne(optional = true)
  @JsonSerialize(using = ToStringSerializer::class)
  var user: User? = null,

  @Column
  @Enumerated(EnumType.STRING)
  var type: ActivityType? = null,

  @Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
  var created: Calendar? = null,

  @JsonIgnore
  @ManyToOne
  @JoinTable(
    name = "conferences_activities",
    joinColumns = arrayOf(JoinColumn(name = "activity_id")),
    inverseJoinColumns = arrayOf(JoinColumn(name = "conference_id"))
  )
  var conference: Conference? = null,

  @JsonIgnore
  @ManyToOne
  @JoinTable(
    name = "pages_activities",
    joinColumns = arrayOf(JoinColumn(name = "activity_id")),
    inverseJoinColumns = arrayOf(JoinColumn(name = "page_id"))
  )
  var page: Page? = null
) {
  constructor (user: User, type: ActivityType) : this(null, user, type, null, null, null)
}
