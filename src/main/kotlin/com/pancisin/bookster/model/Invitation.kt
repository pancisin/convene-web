package com.pancisin.bookster.model

import java.util.Calendar

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import javax.persistence.Table

import org.hibernate.validator.constraints.Email

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonView
import com.pancisin.bookster.models.views.Compact

@Entity
@Table(name = "invitations")
data class Invitation(

  @Id @JsonView(Compact::class) @GeneratedValue(strategy = GenerationType.AUTO)
  val id: Long? = null,

  @Email
  @Column
  var email: String? = null,

  @OneToOne(optional = true)
  var user: User? = null,

  @JsonView(Compact::class)
  @Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
  val created: Calendar? = null,

  @JsonIgnore @ManyToOne
  @JoinTable(
    name = "events_invitations",
    joinColumns = arrayOf(JoinColumn(name = "invitation_id")),
    inverseJoinColumns = arrayOf(JoinColumn(name = "event_id"))
  )
  var event: Event? = null,

  @JsonIgnore @ManyToOne
  @JoinTable(
    name = "pages_invitations",
    joinColumns = arrayOf(JoinColumn(name = "invitation_id")),
    inverseJoinColumns = arrayOf(JoinColumn(name = "page_id"))
  )
  var page: Page? = null
)
