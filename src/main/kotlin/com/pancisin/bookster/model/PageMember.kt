package com.pancisin.bookster.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonView
import com.pancisin.bookster.models.views.Compact
import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "pages_members", uniqueConstraints = arrayOf(UniqueConstraint(columnNames = arrayOf("page_id", "user_id"))))
data class PageMember(

  @Id @GeneratedValue(generator = "uuid2") @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
  val id: UUID? = null,

  @ManyToOne
  var user: User? = null,

  @JsonIgnore @ManyToOne
  var page: Page? = null,

  @OneToOne
  var submission: FormSubmission? = null,

  @Column
  var active: Boolean = true,

  @Column
  var approved: Boolean = false,

  @JsonView(Compact::class)
  @Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
  val created: Calendar? = null

) {
  constructor(user: User, page: Page) : this(null, user, page)
  constructor(user: User, page: Page, submission: FormSubmission) : this(null, user, page, submission)
}
