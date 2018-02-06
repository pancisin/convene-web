package com.pancisin.bookster.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*
import kotlin.collections.ArrayList

@Entity
@Table(name = "forms")
class Form {

  @Id
  @GeneratedValue(generator = "uuid2") @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
  var id: UUID? = null

  @JsonIgnore
  @OneToMany(mappedBy = "form")
  var submissions: MutableList<FormSubmission> = ArrayList()

  @OneToMany(orphanRemoval = true, cascade = arrayOf(CascadeType.ALL))
  var formFields: MutableList<FormField> = ArrayList()
    get
    set(metaFields) {
      this.formFields.clear()
      this.formFields.addAll(metaFields)
    }
}
