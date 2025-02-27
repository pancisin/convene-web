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

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.Access
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.pancisin.bookster.model.dtos.UserDto

@Entity
@Table(name = "forms_submissions")
data class FormSubmission(

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  val id: Long? = null,

  @JsonIgnore
  @ManyToOne
  val user: User? = null,

  @JsonIgnore
  @ManyToOne
  val form: Form? = null,

  @OneToMany(cascade = [ CascadeType.ALL ])
  var values: MutableList<FormFieldValue>? = null,

  @Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
  val created: Calendar? = null
) {
  val userDto: UserDto?
    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "user")
    get () {
      val us = this.user
      return if (us !== null) UserDto.fromUserModel(us) else null
    }
}
