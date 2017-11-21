package com.pancisin.bookster.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pancisin.bookster.components.SitemapView;

@Controller
public class SitemapController {

	@Autowired
	private SitemapView sitemapView;
	
	@RequestMapping(path = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE) 
	public SitemapView create() {
		return sitemapView;
	}
}
