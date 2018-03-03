package com.pancisin.bookster.model

import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Id
import com.pancisin.bookster.model.enums.RecipientType
import javax.persistence.GeneratedValue
import javax.persistence.ManyToOne
import javax.persistence.Enumerated
import javax.persistence.EnumType
import javax.persistence.Column
import java.util.Calendar
import javax.persistence.GenerationType

@Entity
@Table(name = "messages")
data class Message(

  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long? = null,

  @ManyToOne
  var sender: User? = null,

  @Enumerated(EnumType.STRING)
  var recipientType: RecipientType? = null,

  @Column
  var recipientId: Long? = null,

  @Column
  var content: String? = null,

  @Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
  var created: Calendar = Calendar.getInstance()
)
