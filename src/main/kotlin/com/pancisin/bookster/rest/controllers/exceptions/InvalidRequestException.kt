package com.pancisin.bookster.rest.controllers.exceptions

import org.springframework.validation.Errors

class InvalidRequestException(message: String, val errors: Errors) : RuntimeException(message)
