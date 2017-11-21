package com.pancisin.bookster.components;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pancisin.bookster.models.enums.PageState;
import com.pancisin.bookster.models.enums.Visibility;
import com.pancisin.bookster.repository.ConferenceRepository;
import com.pancisin.bookster.repository.EventRepository;
import com.pancisin.bookster.repository.PageRepository;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;

@Service
public class SitemapService {
	private static final String BASE_URL = "https://convene.sk/#";

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private PageRepository pageRepository;

	@Autowired
	private ConferenceRepository conferenceRepository;

	private final String[] staticRoutes = { "about", "pricing", "faq", "terms", "privacy-policy", "events", "explore",
			"conferences" };

	public String createSitemap() throws MalformedURLException {
		WebSitemapGenerator sitemap = new WebSitemapGenerator(BASE_URL);

		List<WebSitemapUrl> pages = pageRepository.findAll().stream()
				.filter(p -> p.getState().equals(PageState.PUBLISHED) || p.getState().equals(PageState.BLOCKED)).map(page -> {
					try {
						String identifier = page.getSlug() == null || page.getSlug().equals("") ? page.getId().toString()
								: page.getSlug();
						String url = String.join("/", BASE_URL, "page", identifier);

						return new WebSitemapUrl(url);
					} catch (MalformedURLException e1) {
						e1.printStackTrace();
						return null;
					}
				}).filter(x -> x != null).collect(Collectors.toList());

		sitemap.addUrls(pages);

		List<WebSitemapUrl> events = eventRepository.findAll().stream().filter(e -> e.getVisibility() == Visibility.PUBLIC)
				.map(event -> {
					try {
						String url = String.join("/", BASE_URL, "event", event.getId().toString());
						return new WebSitemapUrl(url);
					} catch (MalformedURLException e1) {
						return null;
					}
				}).filter(x -> x != null).collect(Collectors.toList());

		sitemap.addUrls(events);

		List<WebSitemapUrl> conferences = conferenceRepository.findAll().stream()
				.filter(c -> c.getState().equals(PageState.PUBLISHED) || c.getState().equals(PageState.BLOCKED))
				.map(conference -> {
					try {
						String url = String.join("/", BASE_URL, "conference", conference.getId().toString());
						return new WebSitemapUrl(url);
					} catch (MalformedURLException e1) {
						return null;
					}
				}).filter(x -> x != null).collect(Collectors.toList());

		sitemap.addUrls(conferences);
		
		List<WebSitemapUrl> staticUrls = Arrays.asList(staticRoutes).stream().map(r -> {
			try {
				String url = String.join("/", BASE_URL, r);
				return new WebSitemapUrl(url);
			} catch (MalformedURLException e1) {
				return null;
			}
		}).filter(x -> x != null).collect(Collectors.toList());
		
		sitemap.addUrls(staticUrls);

		return String.join("", sitemap.writeAsStrings());
	}
}
