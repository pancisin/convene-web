package com.pancisin.bookster.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.pancisin.bookster.model.enums.FieldType
import javax.persistence.*

@Entity @Table(name = "forms_fields")
data class FormField(
  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  val id: Long? = null,

  @Column
  var name: String = "",

  @Enumerated(EnumType.STRING)
  var type: FieldType = FieldType.TEXT,

  @Column
  var description: String = "",

  @JsonIgnore @OneToMany(mappedBy = "field")
  var values: List<FormFieldValue>? = null,

  @ElementCollection
  @CollectionTable(name = "forms_fields_options")
  @Column
  var options: List<String> = ArrayList(),

  @Column
  var optional: Boolean = false,

  @Column
  var ordering: Int = 0
) {
  override fun toString(): String {
    return this.id.toString()
  }
}
