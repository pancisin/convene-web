package com.pancisin.bookster.rest.controllers;

import java.security.Principal;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.models.EventBot;
import com.pancisin.bookster.models.Media;
import com.pancisin.bookster.models.Page;
import com.pancisin.bookster.models.PageAdministrator;
import com.pancisin.bookster.models.User;
import com.pancisin.bookster.models.enums.BotRunState;
import com.pancisin.bookster.models.enums.PageRole;
import com.pancisin.bookster.repository.EventBotRepository;
import com.pancisin.bookster.repository.PageAdministratorRepository;
import com.pancisin.bookster.repository.PageRepository;
import com.pancisin.bookster.repository.UserRepository;

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

	@Autowired
	private SimpMessagingTemplate webSocket;

	@Autowired
	private EventBotRepository eventBotRepository;

	@Autowired
	private PageAdministratorRepository paRepository;
	
	@Autowired
	private UserRepository userRepository;

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

	@GetMapping("/convert")
	public ResponseEntity<?> importPage(@RequestParam(name = "facebook_id") String facebook_id) {

		Facebook fb = new FacebookFactory().getInstance();

		try {
			fb.setOAuthAccessToken(fb.getOAuthAppAccessToken());

			Reading r = new Reading();
			r.fields("name", "about", "cover", "location", "picture");

			facebook4j.Page fb_page = fb.getPage(facebook_id, r);
			Page page = convertPage(fb_page);

			return ResponseEntity.ok(page);
		} catch (FacebookException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Transactional
	@MessageMapping("/page-import")
	public void runEventBot(@Payload Map<String, String> requestMap, Principal principal) {
		String facebook_id = requestMap.get("facebook_id");

		if (facebook_id != null && !facebook_id.equals("")) {
			try {
				webSocket.convertAndSendToUser(principal.getName(), "/queue/page.import", BotRunState.RUNNING);
				Facebook fb = new FacebookFactory().getInstance();
				fb.setOAuthAccessToken(fb.getOAuthAppAccessToken());

				Reading r = new Reading();
				r.fields("name", "about", "cover", "location", "picture");

				Page page = convertPage(fb.getPage(facebook_id, r));
				page = pageRepository.save(page);

				EventBot bot = new EventBot(page, facebook_id);
				bot.setActive(true);
				eventBotRepository.save(bot);

				User user = userRepository.findByEmail(principal.getName());
				
				PageAdministrator pa = new PageAdministrator(page, user, true);
				pa.setRole(PageRole.ROLE_OWNER);
				paRepository.save(pa);

				if (page != null) {
					webSocket.convertAndSendToUser(principal.getName(), "/queue/page.import", BotRunState.SUCCESS);
					webSocket.convertAndSendToUser(principal.getName(), "/queue/page.import", page);
				}
			} catch (FacebookException e) {
				e.printStackTrace();
			}
		}
	}

	private Page convertPage(facebook4j.Page fb_page) {
		Page page = new Page();
		page.setName(fb_page.getName());
		page.setSummary(fb_page.getAbout());
		page.setFacebookId(fb_page.getId());
		page.setPoster(new Media(fb_page.getPicture().toString()));
		return page;
	}
}
