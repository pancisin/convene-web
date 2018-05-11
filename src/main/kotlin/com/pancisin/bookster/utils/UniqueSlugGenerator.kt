package com.pancisin.bookster.utils

import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

import org.hibernate.Session
import org.hibernate.tuple.ValueGenerator

import com.github.slugify.Slugify
import com.pancisin.bookster.model.Page

class UniqueSlugGenerator : ValueGenerator<String> {

  override fun generateValue(session: Session, owner: Any): String? {
    val page = owner as Page
    var slug = Slugify().slugify(page.name)

    session.doWork { connection ->
      try {
        val query = "select count(id) as slug_count from pages WHERE slug LIKE '$slug%'"
        val rs = connection.createStatement().executeQuery(query)

        if (rs.next()) {
          val number = rs.getInt(1)

          if (number > 0) {
            slug = arrayOf(slug, rs.getInt(1).toString()).joinToString("-")
          }
        }
      } catch (ex: SQLException) {
        ex.printStackTrace()
      }
    }

    return slug
  }

}
