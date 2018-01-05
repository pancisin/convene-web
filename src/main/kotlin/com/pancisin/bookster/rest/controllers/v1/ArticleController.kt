package com.pancisin.bookster.rest.controllers.v1

import com.pancisin.bookster.model.Article
import com.pancisin.bookster.model.Media
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import com.pancisin.bookster.components.storage.StorageServiceImpl
import com.pancisin.bookster.repository.ArticleRepository
import com.pancisin.bookster.repository.MediaRepository

@RestController
@RequestMapping("/api/v1/article/{article_id}")
class ArticleController {

  @Autowired
  lateinit var articleRepository: ArticleRepository

  @Autowired
  lateinit var storageService: StorageServiceImpl

  @Autowired
  lateinit var mediaRepository: MediaRepository

  @GetMapping
  fun getArticle(@PathVariable article_id: Long) = ResponseEntity.ok(articleRepository.findOne(article_id))

  @PutMapping
  fun putArticle(@PathVariable article_id: Long, @RequestBody article: Article): ResponseEntity<Article> {
    val stored = articleRepository.findOne(article_id).apply {
      content = article.content;
      title = article.title
    }

    article.thumbnailData?.let {
      if (storageService.isBinary(article.thumbnailData)) {
        var thumbnail = mediaRepository.save(Media())

        val url = "banners/articles/" + thumbnail.id!!.toString()

        thumbnail.path = "/files/$url.jpg"
        storageService.storeBinary(article.thumbnailData, url)
        stored.thumbnail = thumbnail
      }
    }

    return ResponseEntity.ok(articleRepository.save(stored))
  }

  @DeleteMapping
  fun deleteArticle(@PathVariable article_id: Long?): ResponseEntity<String> {
    articleRepository.delete(article_id)
    return ResponseEntity.ok("success")
  }

  @PatchMapping("/toggle-published")
  fun togglePublished(@PathVariable article_id: Long?): ResponseEntity<Article> {
    val stored = articleRepository.findOne(article_id)
    stored.published = !stored.published
    return ResponseEntity.ok(articleRepository.save(stored))
  }
}
