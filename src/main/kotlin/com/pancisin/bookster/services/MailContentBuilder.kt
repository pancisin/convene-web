package com.pancisin.bookster.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

import com.pancisin.bookster.model.User
import com.pancisin.bookster.model.UserSubscription

@Service
class MailContentBuilder @Autowired
constructor(private val templateEngine: TemplateEngine) {

  fun build(header: String, content: String) = templateEngine.process("mailTemplate", Context().apply {
    setVariable("header", header)
    setVariable("content", content)
  })

  fun buildInvoice(us: UserSubscription) = templateEngine.process("invoiceEmailTemplate", Context().apply {
    setVariable("us", us)
  })

  fun buildVerification(user: User, url: String) = templateEngine.process("verificationEmailTemplate", Context().apply {
    setVariable("user", user)
    setVariable("url", url)
  })
}
