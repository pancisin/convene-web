package com.pancisin.employger.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.http.HttpStatus;

import com.pancisin.employger.utils.EnviromentProvider;

@Controller
@RequestMapping("/")
public class IndexController {
	@Autowired
	EnviromentProvider environmentProvider;

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping("/")
	public ModelAndView mainPage() {
		if (environmentProvider.isProduction()) {
			return new ModelAndView("forward://dist/index.html");
		} else {
			return new ModelAndView("forward://index.html");
		}
	}
}
