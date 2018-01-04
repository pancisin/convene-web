package com.pancisin.bookster.models.enums

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonView
import com.pancisin.bookster.models.views.Summary

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class Locale private constructor(@get:JsonView(Summary::class)
                                      val name: String, @get:JsonView(Summary::class)
                                      val code: String, @get:JsonView(Summary::class)
                                      val dateFormat: String, @get:JsonView(Summary::class)
                                      val timeFormat: String) {
    sk("sk", "locale.sk", "DD.MM.YYYY", "HH:mm"), en("en", "locale.en", "MM/DD/YYYY", "H mm a")
}
