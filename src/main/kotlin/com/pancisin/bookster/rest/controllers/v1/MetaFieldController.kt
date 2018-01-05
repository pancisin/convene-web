package com.pancisin.bookster.rest.controllers.v1

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import com.pancisin.bookster.model.MetaField
import com.pancisin.bookster.repository.MetaFieldRepository

@RestController
@RequestMapping("/api/v1/meta-field/{field_id}")
class MetaFieldController {

  @Autowired
  lateinit var cmfRepository: MetaFieldRepository

  @GetMapping
  fun getField(@PathVariable field_id: Long) = ResponseEntity.ok(cmfRepository.findOne(field_id))

  @PutMapping
  fun putField(@PathVariable field_id: Long?, @RequestBody field: MetaField): ResponseEntity<MetaField> {
    val stored = cmfRepository.findOne(field_id).apply {
      name = field.name;
      type = field.type;
      description = field.description;
      values = field.values;
      optional = field.optional;
      ordering = field.ordering;
    }

    return ResponseEntity.ok(cmfRepository.save(stored))
  }

  @DeleteMapping
  fun deleteField(@PathVariable field_id: Long?): ResponseEntity<String> {
    cmfRepository.delete(field_id)
    return ResponseEntity.ok("success")
  }
}
