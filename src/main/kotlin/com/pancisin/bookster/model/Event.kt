package com.pancisin.bookster.model

import java.math.BigDecimal
import java.sql.Time
import java.util.ArrayList
import java.util.Calendar
import java.util.HashMap

import javax.validation.constraints.NotNull

import org.hibernate.validator.constraints.NotEmpty
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.Access
import com.fasterxml.jackson.annotation.JsonView
import com.pancisin.bookster.model.dtos.UserDto
import com.pancisin.bookster.model.enums.PageState
import com.pancisin.bookster.model.enums.Visibility
import com.pancisin.bookster.model.interfaces.IAuthor
import com.pancisin.bookster.models.views.Compact
import com.pancisin.bookster.models.views.Summary
import org.hibernate.search.annotations.Field
import org.hibernate.search.annotations.Indexed
import javax.persistence.*

@Entity
@Indexed
@Table(name = "events")
class Event() {

  @Id
  @JsonView(Compact::class)
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long? = null

  @Column
  @NotEmpty
  @Field
  @JsonView(Compact::class)
  var name: String? = null

  @Column
  @NotNull
  @JsonView(Summary::class)
  var date: Calendar? = null

  @JsonIgnore
  @ManyToOne
  var owner: User? = null

  @NotNull
  @Enumerated(EnumType.STRING)
  @JsonView(Summary::class)
  var visibility: Visibility? = null

  @JsonView(Summary::class)
  @Enumerated(EnumType.STRING)
  var state: PageState = PageState.DEACTIVATED

  @Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
  var created: Calendar? = null

  @OneToMany(mappedBy = "event")
  @OrderBy("time")
  @JsonIgnore
  var programme: List<Programme>? = null

  @JsonIgnore
  @ManyToOne
  @JoinTable(
    name = "pages_events",
    joinColumns = arrayOf(JoinColumn(name = "event_id")),
    inverseJoinColumns = arrayOf(JoinColumn(name = "page_id"))
  )
  var page: Page? = null

  @Lob
  @Column
  var summary: String? = null

  @Column(unique = true)
  var facebookId: String? = null

  @JsonIgnore
  @ManyToMany
  var attendees: MutableList<User> = ArrayList()

  @JsonProperty(access = Access.READ_ONLY)
  @JsonView(Summary::class)
  @OneToOne(optional = true, cascade = arrayOf(CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH), orphanRemoval = true)
  var poster: Media? = null

  @OneToOne(optional = true, cascade = arrayOf(CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH), orphanRemoval = true)
  @JsonView(Summary::class)
  var banner: Media? = null

  @Transient
  @JsonProperty(access = Access.WRITE_ONLY)
  var posterData: String? = null

  @OneToOne
  var place: Place? = null

  val startsAt: Time?
    @JsonProperty(required = false)
    get() = this.programme?.minBy { p -> p.time!!.time }?.time

  @JsonIgnore
  @OneToMany(mappedBy = "event")
  val invitations: List<Invitation>? = null

  @JsonIgnore
  @OneToMany(fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST))
  private var gallery: MutableList<Media>? = null

  @Column(precision = 10, scale = 8)
  @JsonView(Summary::class)
  var latitude: BigDecimal? = null

  @Column(precision = 10, scale = 8)
  @JsonView(Summary::class)
  var longitude: BigDecimal? = null

  @JsonProperty(access = Access.READ_ONLY)
  var featured: Boolean = false

  @ElementCollection
  @CollectionTable(name = "events_tags")
  var tags: MutableList<String> = ArrayList()

  @OneToMany(mappedBy = "event", orphanRemoval = true)
  @JsonIgnore
  var ratings: List<Rating> = ArrayList()

  val author: IAuthor?
    get() {
      if (page !== null) return page else {
        val us = this.owner

        if (us !== null) return UserDto.fromUserModel(us)
      }

      return null
    }

  val averageRating
    get() = if (ratings.size > 0) ratings.sumBy { it.starsCount } / ratings.size else 0

  val privilege: Any?
    @Transient
    @JsonView(Summary::class)
    @JsonIgnoreProperties("user")
    get() {
      if (SecurityContextHolder.getContext().authentication != null
        && SecurityContextHolder.getContext().authentication.isAuthenticated
        && SecurityContextHolder.getContext().authentication !is AnonymousAuthenticationToken) {
        val user = SecurityContextHolder.getContext().authentication.principal as User

        return page?.privilege ?: if (owner?.id === user.id) hashMapOf("active" to true) else null
      }

      return false
    }

  val attendeesCount: Int
    get() = this.attendees.size

  fun getGallery(): List<Media>? {
    return gallery
  }

  fun addGallery(media: Media) {
    if (this.gallery == null) {
      this.gallery = ArrayList()
    }

    this.gallery!!.add(media)
  }
}
