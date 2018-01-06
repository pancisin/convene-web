package com.pancisin.bookster.rest.controllers.v1

import java.util.UUID

import com.pancisin.bookster.model.Media
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import com.pancisin.bookster.repository.MediaRepository

@RestController
@RequestMapping("/api/v1/media/{media_id}")
class MediaController {

  @Autowired
  lateinit var mediaRepository: MediaRepository

  @GetMapping
  fun getMedia(@PathVariable media_id: UUID) = ResponseEntity.ok(mediaRepository.findById(media_id))

  @DeleteMapping
  fun deleteMedia(@PathVariable media_id: UUID): ResponseEntity<*> {
    val stored = mediaRepository.findById(media_id).apply {
      deleted = true;
    }
    return ResponseEntity.ok(mediaRepository.save(stored))
  }
}
