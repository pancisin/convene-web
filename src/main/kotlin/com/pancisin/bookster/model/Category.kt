package com.pancisin.bookster.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonView
import com.pancisin.bookster.model.Branch
import com.pancisin.bookster.models.views.Summary

@Entity @Table(name = "categories")
data class Category(
  @Id @JsonView(Summary::class) @GeneratedValue(strategy = GenerationType.AUTO)
  val id: Long? = null,

  @Column
  val name: String? = null,

  @JsonIgnore @OneToMany(mappedBy = "category")
  val branches: List<Branch>? = null,

  @Column @JsonView(Summary::class)
  val code: String? = null
)
