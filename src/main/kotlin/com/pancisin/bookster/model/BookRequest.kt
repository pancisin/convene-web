package com.pancisin.bookster.model

import java.util.Calendar

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
@Table(name = "book_requests")
data class BookRequest(

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
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
  var approved: Boolean? = null,

  @OneToOne
  var submission: FormSubmission? = null

) {
  val email: String?
    get() = user?.email
}
