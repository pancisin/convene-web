package com.pancisin.bookster.utils.configurations

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "jwt")
class JwtConfiguration {

  var header: String? = null
  var secret: String? = null
  var verificationSecret: String? = null

}
