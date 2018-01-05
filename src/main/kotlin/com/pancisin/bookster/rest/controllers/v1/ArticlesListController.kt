package com.pancisin.bookster.rest.controllers.v1

import java.util.UUID

import javax.transaction.Transactional
import javax.validation.Valid

import com.pancisin.bookster.model.Article
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort.Direction
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import com.pancisin.bookster.model.ArticleBot
import com.pancisin.bookster.model.ArticlesList
import com.pancisin.bookster.repository.ArticleBotRepository
import com.pancisin.bookster.repository.ArticleRepository
import com.pancisin.bookster.repository.ArticlesListRepository
import org.springframework.data.domain.Page

@RestController
@RequestMapping("/api/v1/articles-list/{articlesListId}")
class ArticlesListController {

  @Autowired
  lateinit var alRepository: ArticlesListRepository

  @Autowired
  lateinit var articleRepository: ArticleRepository

  @Autowired
  lateinit var abRepository: ArticleBotRepository

  @GetMapping
  fun getArticlesList(@PathVariable articlesListId: UUID) = ResponseEntity.ok(alRepository.findOne(articlesListId))

  @PutMapping
  fun putArticlesList(
    @PathVariable articlesListId: UUID,
    @RequestBody @Valid articlesList: ArticlesList,
    bindingResult: BindingResult
  ): ResponseEntity<ArticlesList> {

    if (bindingResult.hasErrors()) { }

    val stored = alRepository.findOne(articlesListId).apply {
      name = articlesList.name;
      tags = articlesList.tags;
    }

    return ResponseEntity.ok(alRepository.save(stored))
  }

  @GetMapping("/article/{page}/{size}")
  fun getArticles(
    @PathVariable articlesListId: UUID,
    @PathVariable page: Int,
    @PathVariable size: Int
  ): ResponseEntity<Page<Article>> {
    return ResponseEntity.ok(articleRepository.getByArticlesList(articlesListId, PageRequest(page, size, Direction.DESC, "created")))
  }

  @Transactional
  @PostMapping("/article")
  fun postArticle(
    @PathVariable articlesListId: UUID,
    @RequestBody article: Article
  ): ResponseEntity<Article> {
    val stored = alRepository.findOne(articlesListId)
    article.articlesList = stored

    return ResponseEntity.ok(articleRepository.save(article))
  }

  @PostMapping("/bot")
  fun postArticleBot(
    @PathVariable articlesListId: UUID,
    @RequestBody bot: ArticleBot
  ): ResponseEntity<ArticleBot> {
    val stored = alRepository.findOne(articlesListId)
    bot.articlesList = stored
    return ResponseEntity.ok(abRepository.save(bot))
  }

  @GetMapping("/bot/{page}/{size}")
  fun getArticleBots(
    @PathVariable articlesListId: UUID,
    @PathVariable page: Int,
    @PathVariable size: Int
  ): ResponseEntity<Page<ArticleBot>> {
    return ResponseEntity.ok(abRepository.getByList(articlesListId, PageRequest(page, size)))
  }
}
