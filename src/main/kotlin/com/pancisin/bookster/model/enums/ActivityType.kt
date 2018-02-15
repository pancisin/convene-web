package com.pancisin.bookster.model.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class ActivityType private constructor(

  @JsonProperty("name")
  val action: String,

  @JsonProperty("code")
  val code: String,

  @JsonProperty("public")
  val public: Boolean = true

) {
  NOT_SPECIFIED("FOLLOWING", "activity.type.following"),

  CREATE("CREATE", "activity.type.create"),
  UPDATE("UPDATE", "activity.type.update", false),
  DELETE("DELETE", "activity.type.delete"),
  PUBLISH("PUBLISJED", "activity.type.publish"),
  FEATURE("FEATURE", "activity.type.feature"),

  FOLLOWING("FOLLOWING", "activity.type.following", false),
  ATTENDING("ATTENDING", "activity.type.attending", false),

  CREATE_EVENT("CREATE_EVENT", "activity.type.create_event"),
  CREATE_SERVICE("CREATE_SERVICE", "activity.type.create_service"),
  CREATE_ADMINISTRATOR("CREATE_ADMINISTRATOR", "activity.type.create_administrator", false),
  CREATE_PLACE("CREATE_PLACE", "activity.type.create_place", false),
  CREATE_ARTICLE("CREATE_ARTICLE", "activity.type.create_article"),
  CREATE_SURVEY("CREATE_SURVEY", "activity.type.create_survey"),
  CREATE_PROGRAMME("CREATE_PROGRAMME", "activity.type.create.programme"),

  POST_MEDIA("POST_MEDIA", "activity.type.post_media")
}
