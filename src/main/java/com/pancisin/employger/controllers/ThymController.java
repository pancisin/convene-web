package com.pancisin.employger.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ThymController {

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model, String error, String logout) {
		return "login";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String registration(Model model) {
//		model.addAttribute("userForm", new Admin());
		return "register";
	}

//	@RequestMapping(value = "/register", method = RequestMethod.POST)
//	public String registration(@ModelAttribute("userForm") Admin userForm, BindingResult bindingResult, Model model) {
//		adminValidator.validate(userForm, bindingResult);
//		if (bindingResult.hasErrors()) {
//			return "register";
//		}
//		adminService.save(userForm);
//		securityService.autologin(userForm.getUid(), userForm.getPasswordConfirm());
//		return "redirect:/";
//	}
}
