package com.pancisin.bookster.model

import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

import com.fasterxml.jackson.annotation.JsonIgnore
import com.pancisin.bookster.model.enums.MetaType

@Entity @Table(name = "meta_fields")
data class MetaField(
  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  val id: Long? = null,

  @Column
  var name: String? = null,

  @Enumerated(EnumType.STRING)
  var type: MetaType? = null,

  @Column
  var description: String? = null,

  @JsonIgnore @OneToMany(mappedBy = "field")
  var values: List<MetaValue>? = null,

  @ElementCollection @Column
  var options: List<String>? = null,

  @Column
  var optional: Boolean = false,

  @Column
  var ordering: Int = 0
) {
  override fun toString(): String {
    return this.id.toString()
  }
}
