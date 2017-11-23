package com.pancisin.bookster.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pancisin.bookster.repository.EventRepository;
import com.pancisin.bookster.repository.PageRepository;

@Controller
public class StaticController {

	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private PageRepository pageRepository;
	
	@RequestMapping("/static/event/{event_id}")
	public String getEvent(@PathVariable Long event_id, Model model) {
		model.addAttribute("entity", eventRepository.findOne(event_id));
		return "crawlerBotStaticTemplate";
	}
	
	@RequestMapping("/static/page/{page_id}")
	public String getPage(@PathVariable Long page_id, Model model) {
		model.addAttribute("entity", pageRepository.findOne(page_id));
		return "crawlerBotStaticTemplate";
	}
}
