package com.pancisin.bookster.model

import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Id
import javax.persistence.GeneratedValue
import org.hibernate.annotations.GenericGenerator
import javax.persistence.Column
import java.util.UUID
import javax.persistence.ManyToOne
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import javax.persistence.Enumerated
import com.pancisin.bookster.model.enums.ActivityType
import java.util.Calendar
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.pancisin.bookster.model.enums.ObjectType
import com.pancisin.bookster.repository.MediaRepository
import com.pancisin.bookster.utils.EntityTransformUtils
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import javax.persistence.JoinTable
import javax.persistence.JoinColumn
import javax.persistence.EnumType
import javax.persistence.Transient

@Entity
@Table(name = "activities")
data class Activity(

  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
  var id: UUID? = UUID.randomUUID(),

  @ManyToOne(optional = true)
  @JsonSerialize(using = ToStringSerializer::class)
  var user: User? = null,

  @Column
  @Enumerated(EnumType.STRING)
  var type: ActivityType? = null,

  @JsonIgnore
  @ManyToOne
  @JoinTable(
    name = "pages_activities",
    joinColumns = arrayOf(JoinColumn(name = "activity_id")),
    inverseJoinColumns = arrayOf(JoinColumn(name = "page_id"))
  )
  var page: Page? = null,

  @JsonIgnore
  @ManyToOne
  @JoinTable(
    name = "events_activities",
    joinColumns = arrayOf(JoinColumn(name = "activity_id")),
    inverseJoinColumns = arrayOf(JoinColumn(name = "event_id"))
  )
  var event: Event? = null,

  @Enumerated(EnumType.STRING)
  @JsonProperty("object_type")
  var objectType: ObjectType? = ObjectType.PAGE,

  @JsonProperty("object_id")
  var objectId: String? = "",

  @Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
  var created: Calendar? = null
) {
  val subject: Any?
    @Transient
    get() {
      if (page != null) {
        return  EntityTransformUtils.hashMapOfEntity(Page::class.java, page!!)
      } else if (event != null) {
        return EntityTransformUtils.hashMapOfEntity(Event::class.java, event!!)
      }

      return null;
    }

  @Transient
  var objectThumbnail: Any? = null
}
