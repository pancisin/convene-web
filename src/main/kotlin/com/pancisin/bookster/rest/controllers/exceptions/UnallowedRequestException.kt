package com.pancisin.bookster.rest.controllers.exceptions

import org.springframework.validation.Errors

class UnallowedRequestException(message: String) : RuntimeException(message)
