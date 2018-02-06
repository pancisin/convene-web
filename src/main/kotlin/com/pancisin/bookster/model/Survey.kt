package com.pancisin.bookster.model

import java.util.Date

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.Access
import com.pancisin.bookster.model.enums.SurveyState
import javax.persistence.*

@Entity
@Table(name = "surveys")
class Survey() {
  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  val id: Long? = null

  @Column
  var name: String? = null

  @Column
  var start_date: Date? = null

  @Column
  var end_date: Date? = null

  @Enumerated(EnumType.STRING) @JsonProperty(access = Access.READ_ONLY)
  var state: SurveyState = SurveyState.NEW

  @OneToOne(cascade = [ CascadeType.ALL ], orphanRemoval = true)
  var form: Form = Form()

  @JsonIgnore
  @ManyToOne
  @JoinTable(
    name = "pages_surveys",
    joinColumns = arrayOf(JoinColumn(name = "survey_id")),
    inverseJoinColumns = arrayOf(JoinColumn(name = "page_id"))
  )
  var page: Page? = null

  val submissionsCount
    get() = form.submissions.size
}
