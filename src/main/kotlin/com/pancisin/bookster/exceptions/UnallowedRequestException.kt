package com.pancisin.bookster.exceptions

import org.springframework.validation.Errors

class UnallowedRequestException(message: String) : RuntimeException(message)
