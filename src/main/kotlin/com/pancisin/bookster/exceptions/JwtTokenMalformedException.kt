package com.pancisin.bookster.exceptions

import org.springframework.security.core.AuthenticationException

class JwtTokenMalformedException(msg: String) : AuthenticationException(msg)
