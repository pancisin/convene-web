package com.pancisin.bookster.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.pancisin.bookster.model.enums.MetaType
import javax.persistence.*

@Entity @Table(name = "forms_fields")
data class FormField(
  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  val id: Long? = null,

  @Column
  var name: String? = null,

  @Enumerated(EnumType.STRING)
  var type: MetaType? = null,

  @Column
  var description: String? = null,

  @JsonIgnore @OneToMany(mappedBy = "field")
  var values: List<FormFieldValue>? = null,

  @ElementCollection
  @CollectionTable(name = "forms_fields_options")
  @Column
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
