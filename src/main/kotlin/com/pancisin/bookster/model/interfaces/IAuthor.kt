package com.pancisin.bookster.model.interfaces

import com.fasterxml.jackson.annotation.JsonView
import com.pancisin.bookster.models.views.Summary

//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
//@JsonSubTypes({ @Type(value = Page.class, name = "page"), @Type(value = User.class, name = "user")
//		@Type(value = Conference.class, name = "conference") })
//@JsonSerialize(as = IAuthor.class)
interface IAuthor {

    @get:JsonView(Summary::class)
    val id: Long?

    @get:JsonView(Summary::class)
    val displayName: String

    @get:JsonView(Summary::class)
    val type: String
}
