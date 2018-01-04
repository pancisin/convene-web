package com.pancisin.bookster.model

import java.sql.Time

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull

import com.fasterxml.jackson.annotation.JsonIgnore

@Entity
@Table(name = "programmes")
data class Programme(
  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long? = null,

  @NotNull @Column
  var time: Time? = null,

  @Column
  var duration: Time? = null,

  @NotNull @Column
  var name: String? = null,

  @Column
  var description: String? = null,

  @JsonIgnore @ManyToOne
  var event: Event? = null
)
