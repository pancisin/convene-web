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
    private val articleRepository: ArticleRepository? = null

    @Autowired
    private val botRunRepository: BotRunRepository? = null

    @Autowired
    private val abRepository: ArticleBotRepository? = null

    @Scheduled(cron = "0 30 5 * * *")
    fun run(): Int {
        val bots = abRepository!!.findAll()
        bots.stream().forEach { b ->
            if (b.active)
                this.run(b)
        }

        return 1
    }

    fun run(articleBot: ArticleBot): BotRun {

        try {
            val client = OkHttpClient()
            val request = Request.Builder().url(articleBot.sourceUrl!!).build()
            val response = client.newCall(request).execute()

            val ctx = JsonPath.parse(response.body()!!.string())

            var length = 0
            val contentStore = HashMap<String, List<String>>()
            for ((key, value) in articleBot.parser) {
                val values = ctx.read<List<String>>(value)
                contentStore.put(key, values)

                if (length == 0 || values.size < length) {
                    length = values.size
                }
            }

            var savedArticlesCount = 0

            for (i in 0 until length) {
                val art = Article()

                contentStore.entries.stream().forEach { entry ->
                    try {

                        val field = Article::class.java.getDeclaredField(entry.key)
                        field.isAccessible = true

                        if (field.type.isAssignableFrom(String::class.java) || field.type.isPrimitive) {
                            field.set(art, entry.value[i])
                        } else if (field.type.isAssignableFrom(Calendar::class.java)) {

                        } else {
                            try {
                                val constructor = field.type.getConstructor(String::class.java)
                                field.set(art, constructor.newInstance(entry.value[i]))
                            } catch (e: NoSuchMethodException) {
                                e.printStackTrace()
                            } catch (e: InstantiationException) {
                                e.printStackTrace()
                            } catch (e: InvocationTargetException) {
                                e.printStackTrace()
                            }

                        }
                    } catch (e: NoSuchFieldException) {
                        e.printStackTrace()
                    } catch (e: SecurityException) {
                        e.printStackTrace()
                    } catch (e: IllegalArgumentException) {
                        e.printStackTrace()
                    } catch (e: IllegalAccessException) {
                        e.printStackTrace()
                    }
                }

                art.articlesList = articleBot.articlesList
                try {
                    if (articleRepository!!.save(art) != null) {
                        savedArticlesCount++
                    }
                } catch (ex: ConstraintViolationException) {
                    // ex.printStackTrace();
                } catch (ex: DataIntegrityViolationException) {
                }

            }

            val run = BotRun(articleBot, BotRunState.SUCCESS)
            run.dataCount = savedArticlesCount
            return botRunRepository!!.save(run)
        } catch (e: IOException) {
            e.printStackTrace()

            val run = BotRun(articleBot, BotRunState.ERROR)
            return botRunRepository!!.save(run)
        }

    }
}
