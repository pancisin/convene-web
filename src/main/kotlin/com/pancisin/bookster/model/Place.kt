package com.pancisin.bookster.model

import java.util.ArrayList

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.Transient

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.Access
import com.pancisin.bookster.model.Address
import com.pancisin.bookster.model.Media

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
  private var gallery: MutableList<Media>? = null
) {
  fun getGallery(): List<Media>? {
    return gallery
  }

  fun AddGallery(media: Media) {
    if (this.gallery == null) {
      this.gallery = ArrayList()
    }

    this.gallery!!.add(media)
  }
}
