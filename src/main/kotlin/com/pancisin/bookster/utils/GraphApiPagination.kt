package com.pancisin.bookster.utils

data class GraphApiPagination<T> (
  var content: T? = null,
  var nextCursor: String? = null,
  var previousCursor: String? = null
) {
  constructor(content: T?, nextCursor: String?) : this(content, nextCursor, null)
}
