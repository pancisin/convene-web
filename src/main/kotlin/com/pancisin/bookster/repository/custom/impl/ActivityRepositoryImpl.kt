package com.pancisin.bookster.repository.custom.impl

import com.pancisin.bookster.model.Activity
import com.pancisin.bookster.repository.ActivityRepository
import com.pancisin.bookster.repository.custom.ActivityRepositoryCustom
import com.pancisin.bookster.utils.EntityTransformUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.lang.reflect.Field
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.Id

class ActivityRepositoryImpl : ActivityRepositoryCustom {

  @Autowired
  lateinit var entityManager: EntityManager

  @Autowired
  lateinit var activityRepository: ActivityRepository

  private val hqlQuery = "SELECT activity FROM Activity activity JOIN activity.page page JOIN page.members page_member JOIN page_member.user user WHERE user.id = :user_id AND page_member.active = 1 ORDER BY activity.created DESC"
  private val hqlCountQuery = "SELECT count(*) FROM Activity activity JOIN activity.page page JOIN page.members page_member JOIN page_member.user user WHERE user.id = :user_id AND page_member.active = 1 ORDER BY activity.created DESC"

  override fun getUserActivityFeed(user_id: Long, pageable: Pageable): Page<Activity> {
    val query = entityManager.createQuery(hqlQuery, Activity::class.java)
    query.setParameter("user_id", user_id)

    query.firstResult = pageable.offset
    query.maxResults = pageable.pageSize

    val data = query.resultList.map {
      it.apply {
        val clazz = objectType?.clazz
        val idField = clazz?.declaredFields?.find { it.isAnnotationPresent(Id::class.java) }

        if (idField != null && it.objectId != null && !objectId.equals("")) {
          idField.isAccessible = true
          val stored: Any? = entityManager.find(clazz, fieldTypeCast(idField, it.objectId))

          if (stored != null) {
            objectThumbnail = EntityTransformUtils.hashMapOfEntity(clazz, stored);
          }
        }
      }
    }

    val countQuery = entityManager.createQuery(hqlCountQuery, Long::class.javaObjectType)
    countQuery.setParameter("user_id", user_id)
    val total = countQuery.singleResult

    return PageImpl(data, pageable, total)
  }

  override  fun saveActivity(activity: Activity): Activity {
    activityRepository.save(activity)

    return activity.apply {
      val clazz = objectType?.clazz
      val idField = clazz?.declaredFields?.find { it.isAnnotationPresent(Id::class.java) }

      if (idField != null && activity.objectId != null && !objectId.equals("")) {
        idField.isAccessible = true
        val stored = entityManager.find(clazz, fieldTypeCast(idField, activity.objectId))
        objectThumbnail = EntityTransformUtils.hashMapOfEntity(clazz, stored);
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
}
