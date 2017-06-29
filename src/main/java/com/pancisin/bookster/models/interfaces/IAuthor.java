package com.pancisin.bookster.models.interfaces;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pancisin.bookster.models.views.Summary;

//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
//@JsonSubTypes({ @Type(value = Page.class, name = "page"), @Type(value = User.class, name = "user")
//		@Type(value = Conference.class, name = "conference") })
//@JsonSerialize(as = IAuthor.class)
public interface IAuthor {

	@JsonView(Summary.class)
	Long getId();
	
	@JsonView(Summary.class)
	String getDisplayName();
	
	@JsonView(Summary.class)
	String getType();
}
