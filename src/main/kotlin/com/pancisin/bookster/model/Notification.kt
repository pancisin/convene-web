package com.pancisin.bookster.model

import java.util.Calendar
import java.util.UUID

import org.hibernate.annotations.GenericGenerator

import com.fasterxml.jackson.annotation.JsonIgnore
import com.pancisin.bookster.model.enums.NotificationType
import javax.persistence.*

@Entity
@Table(name = "notifications")
data class Notification(
  @Id @GeneratedValue(generator = "uuid2") @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
  val id: UUID? = null,

  @JsonIgnore @ManyToOne
  var recipient: User? = null,

  @Column
  var code: String? = null,

  @Column
  var seen: Boolean = false,

  @Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
  val created: Calendar? = null,

  @Column
  var target: String? = null,

  @Column
  var subject: String? = null,

  @Column
  @Enumerated(EnumType.STRING)
  var type: NotificationType = NotificationType.INFO
) {
  constructor(code: String, target: String, subject: String) : this(null, null, code, false, null, target, subject)
  constructor(code: String, target: String) : this(null, null, code, false, null, target)
  constructor(code: String) : this(null, null, code)
}
