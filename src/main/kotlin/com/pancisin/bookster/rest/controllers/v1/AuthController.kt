package com.pancisin.bookster.rest.controllers.v1

// import com.pancisin.api.facebookapi.api.FacebookApi
// import com.pancisin.api.facebookapi.utils.Reading

import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

import com.pancisin.bookster.model.Media
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

import com.pancisin.bookster.events.OnRegistrationCompleteEvent
import com.pancisin.bookster.model.User
import com.pancisin.bookster.model.enums.Locale
import com.pancisin.bookster.repository.UserRepository
import com.pancisin.bookster.exceptions.InvalidRequestException
import com.pancisin.bookster.security.models.JwtAuthenticationToken
import com.pancisin.bookster.utils.PasswordUtils
import com.pancisin.bookster.utils.configurations.JwtConfiguration

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts

@RestController
class AuthController {

  @Autowired
  lateinit var jwtConfiguration: JwtConfiguration

  @Autowired
  lateinit var userRepository: UserRepository

  @Autowired
  lateinit var eventPublisher: ApplicationEventPublisher

  @PostMapping("/public/v1/login")
  fun login(@RequestBody user: User, bindingResult: BindingResult): ResponseEntity<User> {
    userRepository.findByEmail(user.email)?.let { stored ->
      user.hashedPassword = PasswordUtils.hashPassword(user.password.toString())
      if (stored.hashedPassword != user.hashedPassword) {
        bindingResult.rejectValue("password", "error.password")
      }

      if (bindingResult.hasErrors()) {
        throw InvalidRequestException("Invalid data", bindingResult)
      }

      return ResponseEntity.ok(stored.apply {
        token = JwtAuthenticationToken.generateToken(stored, jwtConfiguration.secret)
      })
    } ?: run {
      bindingResult.rejectValue("email", "error.user_not_found")
    }

    return ResponseEntity.ok(null)
  }

  @PostMapping("/public/v1/register")
  fun register(
    @Valid @RequestBody user: User,
    bindingResult: BindingResult,
    request: HttpServletRequest
  ): ResponseEntity<*> {

    if (bindingResult.hasErrors())
      throw InvalidRequestException("Invalid data", bindingResult)

    val l = Locale.valueOf(LocaleContextHolder.getLocale().toLanguageTag())

    userRepository.findByEmail(user.email)?.let {
      bindingResult.rejectValue("email", "Duplicate.user.email")
      throw InvalidRequestException("Invalid data", bindingResult)
    } ?: run {
      user.apply {
        locale = l;
        isLocked = false;
        hashedPassword = PasswordUtils.hashPassword(user.password.toString());
        token = JwtAuthenticationToken.generateToken(user, jwtConfiguration.secret)
      }

      userRepository.save(user)
      eventPublisher.publishEvent(OnRegistrationCompleteEvent(user, request.locale, request.scheme + "://" + request.serverName))
      return ResponseEntity.ok(user)
    }
  }

  @PostMapping("/public/v1/login-facebook")
  fun loginFacebook(@RequestBody requestMap: Map<String, String>): ResponseEntity<User> {
    // val api = FacebookApi.Factory.create()
    // val userId = requestMap["userId"]!!

    // api.getUser(userId, Reading().fields("id,name,email,first_name,last_name,locale,picture.width(640)")).execute().body()?.let { user ->
    //   var stored = userRepository.findByFacebookId(userId.toLong()).apply {
    //     token = JwtAuthenticationToken.generateToken(this, jwtConfiguration.secret)
    //   }

    //   if (stored == null) {
    //     stored = userRepository.save(User().apply {
    //       locale = Locale.en
    //       facebookId = userId.toLong()
    //       isLocked = false
    //       firstName = user.firstName
    //       lastName = user.lastName
    //       email = user.email
    //       profilePicture = Media(user.picture?.data?.url.toString())
    //     })
    //   }

    //   return ResponseEntity.ok(stored)
    // }

    return ResponseEntity(HttpStatus.BAD_REQUEST)
  }

  @PutMapping("/public/v1/verify")
  private fun verifyEmail(@RequestBody token: String): ResponseEntity<*> {
    val user: User?
    try {
      val body = Jwts.parser().setSigningKey(jwtConfiguration.verificationSecret).parseClaimsJws(token).body
      user = userRepository.findOne(java.lang.Long.parseLong(body["userId"].toString()))
      user.verified = true
      userRepository.save(user)
    } catch (e: JwtException) {
      System.err.println(e.message)
      return ResponseEntity<String>(HttpStatus.BAD_REQUEST)
    } catch (e: ClassCastException) {
      System.err.println(e.message)
      return ResponseEntity<String>(HttpStatus.BAD_REQUEST)
    }

    return ResponseEntity.ok(user)
  }

  @PostMapping("/api/v1/user/changePassword")
  fun changePassword(@RequestBody requestData: Map<String, String>): ResponseEntity<*> {
    val auth = SecurityContextHolder.getContext().authentication.principal as User
    val stored = userRepository.findOne(auth.id)

    val currentPassword = requestData["currentPassword"].toString()
    val newPassword = requestData["newPassword"].toString()

    if (PasswordUtils.hashPassword(currentPassword) == stored.hashedPassword) {
      userRepository.save(stored.apply {
        hashedPassword = PasswordUtils.hashPassword(newPassword)
      })
    } else {
      return ResponseEntity("Bad data", HttpStatus.BAD_REQUEST)
    }

    return ResponseEntity.ok(stored)
  }
}
