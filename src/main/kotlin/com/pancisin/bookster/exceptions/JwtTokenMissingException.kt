package com.pancisin.bookster.exceptions

import org.springframework.security.core.AuthenticationException

class JwtTokenMissingException(msg: String) : AuthenticationException(msg)
