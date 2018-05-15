package com.pancisin.bookster.exceptions

import org.springframework.validation.Errors

class InvalidRequestException(message: String, val errors: Errors) : RuntimeException(message)
