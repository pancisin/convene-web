package com.pancisin.bookster.components;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.pancisin.bookster.models.Article;
import com.pancisin.bookster.models.ArticleBot;
import com.pancisin.bookster.repository.ArticleBotRepository;
import com.pancisin.bookster.repository.ArticleRepository;
import com.pancisin.bookster.repository.ArticlesListRepository;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Component
public class ArticleBotService {

	@Autowired
	private ArticleBotRepository abRepository;

	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private ArticlesListRepository alRepository;

//	@Scheduled(cron = "0 30 5 * * *")
//	public int run() {
//		List<ArticleBot> bots = abRepository.findAll();
//		bots.stream().forEach(b -> this.run(b));
//		return 1;
//	}

	@Transactional
	public void run(ArticleBot articleBot) {

		try {
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder().url(articleBot.getSourceUrl()).build();
			Response response = client.newCall(request).execute();

			ReadContext ctx = JsonPath.parse(response.body().string());

			Map<String, List<String>> contentStore = new HashMap<String, List<String>>();
			for (Map.Entry<String, String> entry : articleBot.getParser().entrySet()) {
				contentStore.put(entry.getKey(), ctx.read(entry.getValue()));
			}

			List<Article> articles = new ArrayList<Article>();

			for (int i = 0; i <= 10; i++) {
				Article art = new Article();
				final int index = i;

				contentStore.entrySet().stream().forEach(entry -> {
					try {

						Field field = Article.class.getDeclaredField(entry.getKey());
						field.setAccessible(true);
						field.set(art, entry.getValue().get(index));

					} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}

				});

				articles.add(art);
			}

			articleRepository.save(articles);
			articleBot.getArticlesList().addArticles(articles);
			alRepository.save(articleBot.getArticlesList());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
