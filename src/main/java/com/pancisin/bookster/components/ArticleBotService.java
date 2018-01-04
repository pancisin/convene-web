package com.pancisin.bookster.components;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pancisin.bookster.model.Article;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.pancisin.bookster.models.ArticleBot;
import com.pancisin.bookster.models.ArticleBotRun;
import com.pancisin.bookster.models.enums.BotRunState;
import com.pancisin.bookster.repository.ArticleBotRepository;
import com.pancisin.bookster.repository.ArticleBotRunRepository;
import com.pancisin.bookster.repository.ArticleRepository;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Component
public class ArticleBotService {

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ArticleBotRunRepository abRunRepository;

	@Autowired
	private ArticleBotRepository abRepository;

	@Scheduled(cron = "0 30 5 * * *")
	public int run() {
		List<ArticleBot> bots = abRepository.findAll();
		bots.stream().forEach(b -> {
			if (b.isActive())
				this.run(b);
		});

		return 1;
	}

	public ArticleBotRun run(ArticleBot articleBot) {

		try {
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder().url(articleBot.getSourceUrl()).build();
			Response response = client.newCall(request).execute();

			ReadContext ctx = JsonPath.parse(response.body().string());

			int length = 0;
			Map<String, List<String>> contentStore = new HashMap<String, List<String>>();
			for (Map.Entry<String, String> entry : articleBot.getParser().entrySet()) {
				List<String> values = ctx.read(entry.getValue());
				contentStore.put(entry.getKey(), values);

				if (length == 0 || values.size() < length) {
					length = values.size();
				}
			}

			int savedArticlesCount = 0;

			for (int i = 0; i < length; i++) {
				Article art = new Article();
				final int index = i;

				contentStore.entrySet().stream().forEach(entry -> {
					try {

						Field field = Article.class.getDeclaredField(entry.getKey());
						field.setAccessible(true);

						if (field.getType().isAssignableFrom(String.class) || field.getType().isPrimitive()) {
							field.set(art, entry.getValue().get(index));
						} else if (field.getType().isAssignableFrom(Calendar.class)) {

						} else {
							try {
								Constructor<?> constructor = field.getType().getConstructor(String.class);
								field.set(art, constructor.newInstance(entry.getValue().get(index)));
							} catch (NoSuchMethodException | InstantiationException | InvocationTargetException e) {
								e.printStackTrace();
							}
						}
					} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				});

				art.setArticlesList(articleBot.getArticlesList());
				try {
					if (articleRepository.save(art) != null) {
						savedArticlesCount++;
					}
				} catch (ConstraintViolationException | DataIntegrityViolationException ex) {
					// ex.printStackTrace();
				}
			}

			ArticleBotRun run = new ArticleBotRun(articleBot, BotRunState.SUCCESS);
			run.setArticlesCount(savedArticlesCount);
			return abRunRepository.save(run);
		} catch (IOException e) {
			e.printStackTrace();

			ArticleBotRun run = new ArticleBotRun(articleBot, BotRunState.ERROR);
			return abRunRepository.save(run);
		}
	}
}
