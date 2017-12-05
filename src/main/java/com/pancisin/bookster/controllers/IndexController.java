package com.pancisin.bookster.controllers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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

	private final String crawlersPatters = ".*(facebookexternalhit|Facebot|Twitterbot|Pinterest|Google.*snippet|Googlebot).*";
	private final String forwardPaths = "(event|page|conference)";
	
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping("/")
	public String getRoot() {
		return this.mainPage("");
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{leader:(?!stomp|static|api|public|files)[^\\.]*}")
	public String shit(@PathVariable String leader) {
		return this.mainPage(leader);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{leader:(?!stomp|static|api|public|files).*}/**/{path:[^\\.]*}")
	public String mainPage(@PathVariable String leader) {
		String url = request.getRequestURI();

		if (leader.matches(forwardPaths) && request.getHeader("user-agent").matches(crawlersPatters)) {
			Pattern p = Pattern.compile(request.getContextPath());
			Matcher m = p.matcher(url);

			if (m.find()) {
				url = url.substring(m.end(), url.length());
			}

			return String.format("forward:/static/%s", url);
		}

		return "forward:/index.html";
	}

	@GetMapping("/info/user-agent")
	public @ResponseBody String getUserAgent() {
		return request.getHeader("user-agent");
	}

	@GetMapping(path = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
	public SitemapView create() {
		return sitemapView;
	}
}
