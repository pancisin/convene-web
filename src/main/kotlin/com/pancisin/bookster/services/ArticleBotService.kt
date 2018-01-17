package com.pancisin.bookster.services

import java.io.IOException
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.util.Calendar
import java.util.HashMap

import com.pancisin.bookster.model.Article
import com.pancisin.bookster.model.BotRun
import com.pancisin.bookster.repository.BotRunRepository
import org.hibernate.exception.ConstraintViolationException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.ReadContext
import com.pancisin.api.dataparser.parsers.DataParser
import com.pancisin.api.dataparser.parsers.JsonParser
import com.pancisin.bookster.model.ArticleBot
import com.pancisin.bookster.model.enums.BotRunState
import com.pancisin.bookster.repository.ArticleBotRepository
import com.pancisin.bookster.repository.ArticleRepository

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

@Component
class ArticleBotService {

  @Autowired
  lateinit var articleRepository: ArticleRepository

  @Autowired
  lateinit var botRunRepository: BotRunRepository

  @Autowired
  lateinit var abRepository: ArticleBotRepository

  @Scheduled(cron = "0 30 5 * * *")
  fun run(): Int {
    abRepository.findAll().forEach { bot ->
      if (bot.active)
        this.run(bot)
    }
    return 1
  }

  fun run(articleBot: ArticleBot): BotRun {
    try {
      val request = Request.Builder().url(articleBot.sourceUrl.toString()).build()
      val response = OkHttpClient().newCall(request).execute()

      val parser = JsonParser(Article::class.java, articleBot.parser.toString())

      var savedArticlesCount = 0
      parser.parse(response.body()?.string().toString()).let {
        (it as ArrayList<*>).forEach { article ->
          if (article is Article)
            try {
              article.articlesList = articleBot.articlesList
              articleRepository.save(article)
              savedArticlesCount++
            } catch (ex: Exception) {
            }
        }
      }

      return botRunRepository.save(BotRun(articleBot, BotRunState.SUCCESS).apply {
        dataCount = savedArticlesCount
      })
    } catch (e: IOException) {
      e.printStackTrace()

      val run = BotRun(articleBot, BotRunState.ERROR)
      return botRunRepository.save(run)
    }
  }
}
