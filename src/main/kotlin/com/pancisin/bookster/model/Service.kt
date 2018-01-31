package com.pancisin.bookster.model

import javax.validation.constraints.NotNull

import com.fasterxml.jackson.annotation.JsonIgnore
import com.pancisin.bookster.model.enums.Unit
import javax.persistence.*

@Entity
@Table(name = "services")
data class Service(

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long? = null,

  @Column
  var name: String? = null,

  @JsonIgnore
  @ManyToOne
  var page: Page? = null,

  @Column
  var price: Double? = null,

  @Column
  var detail: String? = null,

  @NotNull
  @Enumerated(EnumType.STRING)
  var unit: Unit? = null,

  @OneToOne(cascade = [ CascadeType.ALL ], orphanRemoval = true)
  var form: Form = Form(),

  @JsonIgnore
  @OneToMany(mappedBy = "service", orphanRemoval = true)
  val requests: List<BookRequest>? = null
)
