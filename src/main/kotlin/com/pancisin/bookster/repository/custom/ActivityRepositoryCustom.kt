package com.pancisin.bookster.repository.custom

import com.pancisin.bookster.model.Activity

interface ActivityRepositoryCustom {
  fun getUserActivityFeed(user_id: Long, page: Int, size: Int) : List<Activity>
}
