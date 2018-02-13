package com.pancisin.bookster.repository.custom.impl

import com.pancisin.bookster.model.*
import com.pancisin.bookster.model.enums.ObjectType
import com.pancisin.bookster.repository.custom.ActivityRepositoryCustom
import org.springframework.beans.factory.annotation.Autowired
import java.lang.reflect.Field
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.Id

class ActivityRepositoryImpl : ActivityRepositoryCustom {

  @Autowired
  lateinit var entityManager: EntityManager

  val allowedFields = arrayOf("id", "name", "title", "poster", "pageType", "privilege", "type", "slug", "path", "content", "thumbnail", "summary", "date")

  override fun getUserActivityFeed(user_id: Long, page: Int, size: Int): List<Activity> {
    val builder = entityManager.criteriaBuilder

    val query = entityManager.createQuery("SELECT activity FROM Activity activity JOIN activity.page page JOIN page.members page_member JOIN page_member.user user WHERE user.id = :user_id ORDER BY activity.created DESC", Activity::class.java)
    query.setParameter("user_id", user_id)

    query.firstResult = page * size
    query.maxResults = size

    return query.resultList.map {
      it.apply {
        objectTypeToClass(it.objectType).let { clazz ->
          val idField = clazz?.declaredFields?.find { it.isAnnotationPresent(Id::class.java) }

          if (idField != null && it.objectId != null && !objectId.equals("")) {
            idField.isAccessible = true
            val stored = entityManager.find(clazz, fieldTypeCast(idField, it.objectId))
            objectThumbnail = clazz.declaredFields.mapNotNull {
              if (allowedFields.contains(it.name)) {
                it.isAccessible = true
                it.name to it.get(stored)
              } else {
                null
              }
            }.toMap()
           }
        }
      }
    }
  }

  private fun fieldTypeCast(field: Field, identifier: String?): Any? {
    return when (field.type) {
      Long::class.javaObjectType, Long::class.javaPrimitiveType -> identifier?.toLong()
      UUID::class.java -> UUID.fromString(identifier)
      else -> identifier
    }
  }

  private fun objectTypeToClass(type: ObjectType?): Class<*>? {
    return when (type) {
      ObjectType.PAGE -> Page::class.java
      ObjectType.MEDIA -> Media::class.java
      ObjectType.EVENT -> Event::class.java
      ObjectType.USER -> User::class.java
      ObjectType.SERVICE -> Service::class.java
      ObjectType.ARTICLE -> Article::class.java
      ObjectType.SURVEY -> Survey::class.java
      ObjectType.PROGRAMME -> Programme::class.java
      else -> null
    }
  }
}
