package com.pancisin.bookster.model

import java.util.ArrayList

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.ManyToMany
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.Transient

import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.annotation.JsonProperty.Access
import com.pancisin.bookster.model.enums.PageRole
import com.pancisin.bookster.model.enums.PageState
import com.pancisin.bookster.model.enums.Visibility
import com.pancisin.bookster.model.interfaces.IAuthor
import com.pancisin.bookster.models.views.Summary

@Entity
@Table(name = "conferences")
class Conference : IAuthor {

  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  override var id: Long? = null

  @JsonIgnore @OneToMany(mappedBy = "conference")
  val administrators: List<Administrator>? = null

  @Column
  var name: String? = null

  @JsonIgnore @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
  var metaFields: MutableList<MetaField>? = null

  @JsonIgnore @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
  var surveys: MutableList<Survey>? = null

  @JsonIgnore @OneToMany(mappedBy = "conference")
  val events: List<Event>? = null

  @Enumerated(EnumType.STRING)
  var visibility: Visibility? = null

  @JsonView(Summary::class) @Enumerated(EnumType.STRING)
  var state = PageState.DEACTIVATED

  @Lob @Column
  var summary: String? = null

  @JsonIgnore @OneToMany(mappedBy = "conference")
  val invitations: List<Invitation>? = null

  @JsonIgnore @OneToMany(mappedBy = "conference")
  val attendees: List<ConferenceAttendee>? = null

  @OneToOne(optional = true, cascade = arrayOf(CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH))
  @JsonView(Summary::class)
  var poster: Media? = null

  @Transient @JsonProperty(access = Access.WRITE_ONLY)
  var posterData: String? = null

  @JsonIgnore @OneToMany(mappedBy = "conference")
  var articles: MutableList<Article>? = null

  @JsonIgnore @OneToMany(cascade = arrayOf(CascadeType.MERGE, CascadeType.DETACH), fetch = FetchType.LAZY, orphanRemoval = true)
  var places: MutableList<Place>? = null

  @JsonIgnore @ManyToMany(fetch = FetchType.LAZY, mappedBy = "conference")
  var activities: MutableList<Activity>? = null

  @JsonIgnore
  @OneToMany(orphanRemoval = true, cascade = arrayOf(CascadeType.ALL))
  private var widgets: MutableList<Widget>? = null

  val owner: User?
    @JsonIgnore @Transient
    get() = this.administrators!!.first { a -> a.role === PageRole.ROLE_OWNER }.user

  val privilege: Administrator?
    @Transient
    @JsonView(Summary::class)
    @JsonIgnoreProperties("user")
    get() {
      if (SecurityContextHolder.getContext().authentication != null
        && SecurityContextHolder.getContext().authentication.isAuthenticated
        && SecurityContextHolder.getContext().authentication !is AnonymousAuthenticationToken) {
        val user = SecurityContextHolder.getContext().authentication.principal as User

        return administrators?.firstOrNull { a -> a.user?.id == user.id }
      }

      return null
    }

  val attendeesCount: Int
    get() = this.attendees!!.size

  fun addMetaField(field: MetaField) {
    if (this.metaFields == null)
      this.metaFields = ArrayList()

    this.metaFields!!.add(field)
  }

  fun addSurvey(survey: Survey) {
    if (this.surveys == null)
      this.surveys = ArrayList()

    this.surveys!!.add(survey)
  }

  fun addPlace(place: Place) {
    if (this.places == null)
      this.places = ArrayList()

    this.places!!.add(place)
  }

  override val displayName: String
    get() = this.name.toString()

  override val type: String
    get() = "conference"

  fun getWidgets() : MutableList<Widget>? {
    return widgets
  }

  fun setWidgets(widgets: List<Widget>?) {
    if (this.widgets == null) {
      this.widgets = ArrayList()
    }

    this.widgets!!.clear()

    if (widgets != null) {
      this.widgets!!.addAll(widgets)
    }
  }
}
