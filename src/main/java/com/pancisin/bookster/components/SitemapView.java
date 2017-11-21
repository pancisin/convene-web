package com.pancisin.bookster.components;

import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

@Component
public class SitemapView extends AbstractView {

	@Autowired
	private SitemapService service;

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		response.setContentType(MediaType.APPLICATION_XML_VALUE);

		try (Writer writer = response.getWriter()) {
			writer.append(service.createSitemap());
		}
	}
}
