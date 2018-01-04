package com.pancisin.bookster.model

import java.util.Calendar

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.persistence.UniqueConstraint

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonView
import com.pancisin.bookster.models.Conference
import com.pancisin.bookster.models.User
import com.pancisin.bookster.models.views.Compact

@Entity
@Table(name = "conferences_attendees", uniqueConstraints = arrayOf(UniqueConstraint(columnNames = arrayOf("conference_id", "user_id"))))
data class ConferenceAttendee(

  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  val id: Long? = null,

  @ManyToOne
  var user: User? = null,

  @JsonIgnore @ManyToOne
  var conference: Conference? = null,

  @OneToMany(cascade = arrayOf(CascadeType.ALL))
  var meta: List<MetaValue>? = null,

  @Column
  var active: Boolean = true,

  @Column
  var approved: Boolean = false,

  @JsonView(Compact::class)
  @Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
  val created: Calendar? = null

) {
  constructor(user: User, conference: Conference, meta: List<MetaValue>) : this(null, user, conference, meta)
}
