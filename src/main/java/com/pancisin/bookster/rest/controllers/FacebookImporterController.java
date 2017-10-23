package com.pancisin.bookster.rest.controllers;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.models.EventBot;
import com.pancisin.bookster.models.EventBotRun;
import com.pancisin.bookster.models.Media;
import com.pancisin.bookster.models.Page;
import com.pancisin.bookster.models.enums.BotRunState;
import com.pancisin.bookster.repository.PageRepository;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.GeoLocation;
import facebook4j.Place;
import facebook4j.Reading;
import facebook4j.ResponseList;

@RestController
@RequestMapping("/api/facebook-importer")
public class FacebookImporterController {

	@Autowired
	private PageRepository pageRepository;
	
	@GetMapping("/search")
	public ResponseEntity<?> searchPages(@RequestParam(name = "latitude", required = true) Double latitude,
			@RequestParam(name = "longitude", required = true) Double longitude) {
		Facebook fb = new FacebookFactory().getInstance();

		try {
			fb.setOAuthAccessToken(fb.getOAuthAppAccessToken());

			Reading r = new Reading();
			r.fields("name", "categories", "location", "metadata");

			ResponseList<Place> places = fb.searchPlaces("*", new GeoLocation(latitude, longitude), 1000, r);
			return ResponseEntity.ok(places);
		} catch (FacebookException e) {
			e.printStackTrace();
		}

		return null;
	}

	@PostMapping("/import")
	public ResponseEntity<?> importPage(@RequestParam(name = "facebook_id") String facebook_id) {

		Facebook fb = new FacebookFactory().getInstance();

		try {
			fb.setOAuthAccessToken(fb.getOAuthAppAccessToken());
			
			Reading r = new Reading();
			r.fields("name", "about", "cover", "location", "picture");
			
			facebook4j.Page fb_page = fb.getPage(facebook_id, r);
			
			Page page = new Page();
			page.setName(fb_page.getName());
			page.setSummary(fb_page.getAbout());
			page.setPoster(new Media(fb_page.getPicture().toString()));
			
			return ResponseEntity.ok(page);
		} catch (FacebookException e) {
			e.printStackTrace();
		}

		return null;
	}

	 @MessageMapping("/import")
	 public void runEventBot(@Payload Map<String, String> requestMap, Principal principal)
	 {
		 String facebook_id = requestMap.get("facebook_id");
		 
		 if (facebook_id != null && !facebook_id.equals("")) {
			 Facebook fb = new FacebookFactory().getInstance();

			 
			 fb.setOAuthAccessToken(fb.getOAuthAppAccessToken());
			 Page page = convertPage(fb.getPage(facebook_id));
			 
			 pageRepository
			 
			 webSocket.convertAndSendToUser(principal.getName(), "/queue/page.bots",
			 new EventBotRun(stored, BotRunState.RUNNING));
			
			 EventBotRun run = eventBotService.run(stored);
			 webSocket.convertAndSendToUser(principal.getName(), "/queue/page.bots", run);
		 }
	 }
	 
	 private Page convertPage(facebook4j.Page fb_page) {
		 
		 return null;
	 }
}
