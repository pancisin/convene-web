package com.pancisin.bookster.model

import java.util.UUID

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "widgets")
data class Widget(

  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
  val id: UUID? = null,

  @Column
  var x: Int = 0,

  @Column
  var y: Int = 0,

  @Column
  var w: Int = 0,

  @Column
  var h: Int = 0,

  @Column
  var i: Int = 0,

  @Column
  var type: String? = null
)
