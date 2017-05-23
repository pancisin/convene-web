package com.pancisin.bookster.models.interfaces;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.pancisin.bookster.models.Conference;
import com.pancisin.bookster.models.Page;
import com.pancisin.bookster.models.User;
import com.pancisin.bookster.models.views.Summary;

//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = Page.class, name = "page"), @Type(value = User.class, name = "user"),
		@Type(value = Conference.class, name = "conference") })
public interface IAuthor {

	@JsonView(Summary.class)
	String getDisplayName();
	
	@JsonView(Summary.class)
	String getType();
}
