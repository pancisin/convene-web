package com.pancisin.bookster.model.dtos

import com.pancisin.bookster.model.Media
import com.pancisin.bookster.model.User
import com.pancisin.bookster.model.interfaces.IAuthor

data class UserDto(

  override val id: Long?,

  override val displayName: String,

  override val type: String,

  val profilePicture: Media? = null

) : IAuthor {
  companion object {
    fun fromUserModel(user: User) = UserDto(user.id, user.displayName, "user", user.profilePicture)
  }
}
