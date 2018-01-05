package com.pancisin.bookster.model

import java.util.ArrayList
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

  @OneToMany(orphanRemoval = true, cascade = arrayOf(CascadeType.ALL))
  var metaFields: MutableList<MetaField> = ArrayList()
    get
    set(metaFields) {
      this.metaFields.clear()
      this.metaFields.addAll(metaFields)
    }

  @JsonIgnore @OneToMany(mappedBy = "survey")
  var submissions: MutableList<SurveySubmission>? = null

  @Enumerated(EnumType.STRING) @JsonProperty(access = Access.READ_ONLY)
  var state: SurveyState = SurveyState.NEW

  @JsonIgnore
  @ManyToOne
  @JoinTable(
    name = "conferences_surveys",
    joinColumns = arrayOf(JoinColumn(name = "survey_id")),
    inverseJoinColumns = arrayOf(JoinColumn(name = "conference_id"))
  )
  var conference: Conference? = null

  val submissionsCount
    get() = submissions?.size
}
