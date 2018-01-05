package com.pancisin.bookster.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.Access
import javax.persistence.*

@Entity
@Table(name = "places")
data class Place(
  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  val id: Long? = null,

  @Column(unique = true)
  private val facebookId: String? = null,

  @Column
  var name: String? = null,

  @Column
  var capacity: Int = 0,

  @Column
  var description: String? = null,

  @Column
  @JsonProperty(access = Access.READ_ONLY)
  var venueJsonUrl: String? = null,

  @Transient @JsonProperty(access = Access.WRITE_ONLY)
  var venueData: String? = null,

  @OneToMany(fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST))
  var gallery: MutableList<Media>? = null,

  @JsonIgnore
  @ManyToOne
  @JoinTable(
    name = "pages_places",
    joinColumns = arrayOf(JoinColumn(name = "place_id")),
    inverseJoinColumns = arrayOf(JoinColumn(name = "page_id"))
  )
  var page: Page? = null,

  @JsonIgnore
  @ManyToOne
  @JoinTable(
    name = "conferences_places",
    joinColumns = arrayOf(JoinColumn(name = "place_id")),
    inverseJoinColumns = arrayOf(JoinColumn(name = "conference_id"))
  )
  var conference: Conference? = null
) {
  fun AddGallery(media: Media) {
    this.gallery?.add(media)
  }
}
