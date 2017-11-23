package com.pancisin.bookster.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.pancisin.bookster.components.SitemapView;
import com.pancisin.bookster.utils.EnviromentProvider;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@Controller
public class IndexController {

	@Autowired
	EnviromentProvider environmentProvider;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SitemapView sitemapView;

	private final String crawlersPatters = "facebookexternalhit\\/[0-9](\\.[0-9])?|Facebot|Twitterbot|Pinterest|Google.*snippet";

	@RequestMapping("/")
	public String getRoot() {
		return this.mainPage();
	}
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/**/{[path:[^\\.]*}")
	public String mainPage() {
		final String url = request.getRequestURI();

		if (request.getHeader("user-agent").matches(crawlersPatters) && !url.contains("static")) {
			return String.format("forward://static/%s", url);
		}

		if (environmentProvider.isProduction()) {
			return "forward://dist/index.html";
		} else {
			return "forward://index.html";
		}
	}

	@RequestMapping(path = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
	public SitemapView create() {
		return sitemapView;
	}
}
