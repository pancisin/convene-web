package com.pancisin.bookster.rest.controllers.v1

import com.pancisin.bookster.repository.PageMemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/page-member/{member_id}")
class PageMemberController {

  @Autowired
  lateinit var pageMemberRepository: PageMemberRepository

  @GetMapping
  fun getMember(@PathVariable member_id: UUID) = pageMemberRepository.findOne(member_id)

  @DeleteMapping
  fun deleteMember(@PathVariable member_id: UUID) {
    return pageMemberRepository.delete(member_id)
  }
}
