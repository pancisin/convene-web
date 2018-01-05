package com.pancisin.bookster.model

import java.util.ArrayList
import java.util.Calendar
import java.util.HashMap

import javax.persistence.CascadeType
import javax.persistence.CollectionTable
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.MapKeyColumn
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.Transient

import org.hibernate.annotations.GeneratorType
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.annotation.JsonProperty.Access
import com.pancisin.bookster.model.enums.PageRole
import com.pancisin.bookster.model.enums.PageState
import com.pancisin.bookster.model.interfaces.IAuthor
import com.pancisin.bookster.models.views.Compact
import com.pancisin.bookster.models.views.Summary
import com.pancisin.bookster.utils.UniqueSlugGenerator

@Entity
@Table(name = "pages")
class Page() : IAuthor {

  @Id
  @JsonView(Compact::class)
  @GeneratedValue(strategy = GenerationType.AUTO)
  override var id: Long? = null

  @JsonIgnore
  @OneToMany(mappedBy = "page")
  var administrators: List<Administrator>? = null
    private set

  @Column
  @JsonView(Compact::class)
  var name: String? = null

  @JsonView(Compact::class)
  @JsonProperty(access = Access.READ_ONLY)
  @Column(unique = true, updatable = false)
  @GeneratorType(type = UniqueSlugGenerator::class)
  var slug: String? = null

  @Lob
  @Column
  var summary: String? = null

  @JsonIgnore
  @OneToMany(mappedBy = "page")
  val events: List<Event>? = null

  @ManyToOne
  @JsonView(Summary::class)
  var branch: Branch? = null

  @JsonIgnore
  @ManyToMany
  var followers: MutableList<User> = ArrayList()

  @JsonIgnore
  @OneToMany(mappedBy = "page")
  val services: MutableList<Service> = ArrayList()

  @JsonProperty(access = Access.READ_ONLY)
  @OneToOne(optional = true, cascade = arrayOf(CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH))
  @JsonView(Summary::class)
  var poster: Media? = null

  @Transient
  @JsonProperty(access = Access.WRITE_ONLY)
  var posterData: String? = null

  val followersCount: Int
    @JsonView(Summary::class)
    get() = this.followers.size

  @Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
  private val created: Calendar? = null

  @JsonIgnore
  @OneToMany(mappedBy = "page", fetch = FetchType.LAZY, orphanRemoval = true)
  var places: MutableList<Place>? = null

  val owner: User?
    @JsonIgnore
    get() = administrators?.firstOrNull { a -> a.role === PageRole.ROLE_OWNER }?.user

  @JsonView(Summary::class)
  @Enumerated(EnumType.STRING)
  var state: PageState = PageState.DEACTIVATED

  val category: Category?
    @JsonView(Summary::class)
    get() = this.branch?.category

  @JsonIgnore
  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "page")
  var activities: MutableList<Activity>? = null

  @JsonIgnore
  @OneToMany(orphanRemoval = true, cascade = arrayOf(CascadeType.ALL))
  var widgets: MutableList<Widget> = ArrayList()
    get
    set(widgets) {
      this.widgets.clear()
      this.widgets.addAll(widgets)
    }

  @JsonIgnore
  @OneToMany(fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST))
  var gallery: MutableList<Media>? = null

  @Column(unique = true)
  var facebookId: String? = null

  //	@JsonIgnoreProperties({"user"})
  val privilege: Administrator?
    @Transient
    @JsonView(Summary::class)
    get() {
      if (SecurityContextHolder.getContext().authentication != null
        && SecurityContextHolder.getContext().authentication.isAuthenticated
        && SecurityContextHolder.getContext().authentication !is AnonymousAuthenticationToken) {
        val user = SecurityContextHolder.getContext().authentication.principal as User

        return administrators?.firstOrNull { a -> a.user?.id === user.id }
      }
      return null
    }

  @ElementCollection
  @MapKeyColumn(name = "meta_key")
  @Column(name = "meta_value")
  @CollectionTable(name = "pages_metadata")
  @JsonView(Summary::class)
  var metadata: Map<String, String> = HashMap()

  override val displayName: String
    get() = this.name.toString()

  override val type: String
    get() = "page"

  fun addGallery(media: Media) {
    gallery?.add(media)
  }
}
