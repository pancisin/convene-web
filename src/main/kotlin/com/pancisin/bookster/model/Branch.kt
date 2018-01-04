package com.pancisin.bookster.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonView
import com.pancisin.bookster.models.views.Summary

@Entity @Table(name = "branches")
data class Branch(
  @Id @JsonView(Summary::class) @GeneratedValue(strategy = GenerationType.AUTO)
  val id: Long? = null,

  @Column
  val name: String? = null,

  @JsonIgnore @ManyToOne
  val category: Category? = null,

  @JsonIgnore @OneToMany(mappedBy = "branch")
  private val pages: List<Page>? = null,

  @JsonView(Summary::class) @Column
  val code: String? = null
)
