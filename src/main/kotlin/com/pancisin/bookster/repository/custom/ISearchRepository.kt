package com.pancisin.bookster.repository.custom

interface ISearchRepository {
  fun search(keyword: String) : List<Any>
}
