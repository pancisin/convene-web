package com.pancisin.bookster.utils

class EntityTransformUtils {
  companion object {
    val allowedFields = arrayOf("id", "name", "title", "poster", "pageType", "privilege", "type", "slug", "path", "content", "thumbnail", "summary", "date")

    fun hashMapOfEntity(clazz: Class<*>, entity: Any): Map<String, Any> {
      return clazz.declaredFields
        .filter { f -> allowedFields.contains(f.name) }
        .map {
          it.isAccessible = true
          it.name to it.get(entity)
        }.toMap()
    }
  }
}
