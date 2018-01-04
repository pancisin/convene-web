package com.pancisin.bookster.model

import java.util.ArrayList
import java.util.Date

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.Access
import com.pancisin.bookster.model.enums.SurveyState

@Entity @Table(name = "surveys")
data class Survey(
  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  val id: Long? = null,

  @Column
  var name: String? = null,

  @Column
  var start_date: Date? = null,

  @Column
  var end_date: Date? = null,

  @OneToMany(orphanRemoval = true, cascade = arrayOf(CascadeType.ALL))
  private var metaFields: MutableList<MetaField>? = null,

  @JsonIgnore @OneToMany(mappedBy = "survey")
  var submissions: MutableList<SurveySubmission>? = null,

  @Enumerated(EnumType.STRING) @JsonProperty(access = Access.READ_ONLY)
  var state : SurveyState = SurveyState.NEW
) {
  val submissionsCount
    get() = if (this.submissions != null) this.submissions!!.size else 0

  fun addMetaField(field: MetaField) {
    if (this.metaFields == null)
      this.metaFields = ArrayList()

    this.metaFields!!.add(field)
  }

  fun setMetaFields(metaFields: List<MetaField>?) {
    if (this.metaFields == null)
      this.metaFields = ArrayList()

    this.metaFields!!.clear()

    if (metaFields != null)
      this.metaFields!!.addAll(metaFields)
  }

  fun getMetaFields(): List<MetaField>? {
    return metaFields
  }
}
