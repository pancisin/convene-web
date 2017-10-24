package com.pancisin.bookster.models;

import com.pancisin.bookster.models.enums.BotRunState;

public class PageImport {

	private BotRunState state;
	private Page page;
	
	public PageImport(BotRunState state) {
		this.state = state;
	}

	public PageImport(BotRunState state, Page page) {
		this.state = state;
		this.page = page;
	}
	
	public BotRunState getState() {
		return state;
	}

	public void setState(BotRunState state) {
		this.state = state;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
}
