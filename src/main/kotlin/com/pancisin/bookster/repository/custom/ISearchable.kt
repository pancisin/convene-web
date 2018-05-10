package com.pancisin.bookster.repository.custom

interface ISearchable {
  fun search(keyword: String) : List<Any>
}
