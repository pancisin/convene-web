package com.pancisin.bookster.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.validation.constraints.NotNull

import com.fasterxml.jackson.annotation.JsonIgnore
import com.pancisin.bookster.model.enums.Unit

@Entity
@Table(name = "services")
data class Service(
  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long? = null,

  @Column
  var name: String? = null,

  @JsonIgnore @ManyToOne
  var page: Page? = null,

  @Column
  var pricePerUnit: Double? = null,

  @Column
  var detail: String? = null,

  @NotNull @Enumerated(EnumType.STRING)
  var unit: Unit? = null,

  @JsonIgnore @OneToMany(mappedBy = "service")
  val requests: List<BookRequest>? = null
)
