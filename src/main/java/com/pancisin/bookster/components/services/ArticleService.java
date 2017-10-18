package com.pancisin.bookster.components.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ArticleService {

	@GET
	public Call<ResponseBody> getArticles(@Url String url);
}
