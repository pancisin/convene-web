package com.pancisin.bookster.model

import java.util.Calendar

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

import com.fasterxml.jackson.annotation.JsonIgnore

@Entity @Table(name = "book_requests")
data class BookRequest(
  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  val id: Long? = null,

  @JsonIgnore @ManyToOne
  var user: User? = null,

  @ManyToOne
  var service: Service? = null,

  @Column
  var units: Int = 0,

  @Column
  var date: Calendar? = null,

  @Column
  var approved: Boolean? = null
) {
  val email: String?
    get() = user?.email
}
