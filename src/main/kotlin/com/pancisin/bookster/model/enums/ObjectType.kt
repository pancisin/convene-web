package com.pancisin.bookster.model.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.pancisin.bookster.model.*

enum class ObjectType private constructor(
  @JsonIgnore
  val clazz: Class<*>
) {
  PAGE(Page::class.java),
  EVENT(Event::class.java),
  USER(User::class.java),
  MEDIA(Media::class.java),
  SERVICE(Service::class.java),
  ARTICLE(Article::class.java),
  SURVEY(Survey::class.java),
  PROGRAMME(Programme::class.java)
}
