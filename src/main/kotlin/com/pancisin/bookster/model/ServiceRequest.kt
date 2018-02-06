package com.pancisin.bookster.model

import java.util.Calendar

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonUnwrapped
import javax.persistence.*

@Entity
@Table(name = "service_requests")
data class ServiceRequest(

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  val id: Long? = null,

  @ManyToOne
  @JsonIgnore
  var service: Service? = null,

  @Column
  var units: Int = 0,

  @Column
  var approved: Boolean? = null,

  @OneToOne
  var submission: FormSubmission? = null

)
