package com.pancisin.bookster.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.pancisin.bookster.utils.EnviromentProvider;

import org.springframework.http.HttpStatus;

@Controller
@RequestMapping("/")
public class IndexController {

	@Autowired
	EnviromentProvider environmentProvider;

	@Autowired
	private HttpServletRequest request;

	private final String crawlersPatters = "facebookexternalhit/[0-9]|Twitterbot|Pinterest|Google.*snippet";

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/")
	public ModelAndView mainPage() {

		if (request.getHeader("user-agent").matches(crawlersPatters)) {
			System.out.println(request.getRequestURL());
			return new ModelAndView("forward://static/event/5");
		}

		if (environmentProvider.isProduction()) {
			return new ModelAndView("forward://dist/index.html");
		} else {
			return new ModelAndView("forward://index.html");
		}
	}
}
