package com.pancisin.bookster.repository.custom

import com.pancisin.bookster.model.Activity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ActivityRepositoryCustom {
  fun getUserActivityFeed(user_id: Long, pageable: Pageable) : Page<Activity>
}
