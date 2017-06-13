package com.pancisin.bookster.controllers;

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

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/")
	public ModelAndView mainPage() {
		if (environmentProvider.isProduction()) {
			return new ModelAndView("forward://dist/index.html");
		} else {
			return new ModelAndView("forward://index.html");
		}
	}
}
