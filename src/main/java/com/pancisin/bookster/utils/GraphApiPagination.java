package com.pancisin.bookster.utils;

public class GraphApiPagination<T> {

	private T content;
	private String nextCursor;
	private String previousCursor;
	
	public GraphApiPagination(T content, String nextCursor) {
		this.content = content;
		this.nextCursor = nextCursor;
	}
	
	public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}

	public String getNextCursor() {
		return nextCursor;
	}

	public void setNextCursor(String nextCursor) {
		this.nextCursor = nextCursor;
	}

	public String getPreviousCursor() {
		return previousCursor;
	}

	public void setPreviousCursor(String previousCursor) {
		this.previousCursor = previousCursor;
	}
}
