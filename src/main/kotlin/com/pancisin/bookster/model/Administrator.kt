package com.pancisin.bookster.model

import java.util.Calendar

import javax.validation.constraints.NotNull

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonView
import com.pancisin.bookster.models.Page
import com.pancisin.bookster.models.User
import com.pancisin.bookster.models.enums.PageRole
import com.pancisin.bookster.models.views.Compact
import com.pancisin.bookster.models.views.Summary
import javax.persistence.*

@Entity
@Table(name = "administrators")
data class Administrator(

  @Id @JsonView(Compact::class) @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long? = null,

  @JsonView(Summary::class) @JsonIgnoreProperties("address", "license", "authorities", "created", "token", "locale") @ManyToOne(optional = false, fetch = FetchType.EAGER)
  var user: User? = null,

  @JsonIgnore @ManyToOne
  @JoinTable(
    name = "conferences_administrators",
    joinColumns = arrayOf(JoinColumn(name = "administrator_id")),
    inverseJoinColumns = arrayOf(JoinColumn(name = "conference_id"))
  )
  var conference: Conference? = null,

  @JsonIgnore @ManyToOne
  @JoinTable(
    name = "pages_administrators",
    joinColumns = arrayOf(JoinColumn(name = "administrator_id")),
    inverseJoinColumns = arrayOf(JoinColumn(name = "page_id"))
  )
  var page: Page? = null,

  @NotNull @JsonView(Summary::class) @Column
  var active: Boolean? = false,

  @JsonView(Summary::class) @Column(name = "granted", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
  var granted: Calendar? = null,

  @Column
  var expires: Calendar? = null,

  @NotNull @JsonView(Summary::class) @Enumerated(EnumType.STRING)
  var role: PageRole? = PageRole.ROLE_ADMINISTRATOR

) {
  constructor(page: Page, user: User, active: Boolean) : this(null, user, null, page, active, null, null, null)
  constructor(conference: Conference, user: User, active: Boolean) : this(null, user, conference, null, active, null, null, null)
}
