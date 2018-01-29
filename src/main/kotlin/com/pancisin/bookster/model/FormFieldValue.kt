package com.pancisin.bookster.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

import com.fasterxml.jackson.annotation.JsonRawValue

@Entity
@Table(name = "forms_fields_values")
data class FormFieldValue(

  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  val id: Long? = null,

  @ManyToOne @JsonRawValue
  var field: FormField? = null,

  @Column
  var value: String? = null
)
