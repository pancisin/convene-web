package com.pancisin.bookster.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component

import com.pancisin.bookster.model.User
import com.pancisin.bookster.model.UserSubscription

@Component
class EmailService {

  @Value("\${mailing.enabled}")
  private val enabled: Boolean = false

  @Autowired
  lateinit var sender: JavaMailSender

  @Autowired
  lateinit var builder: MailContentBuilder

  fun sendSimpleMessage(to: String, subject: String, content: String) {
    if (enabled) {
      try {
        sender.send({ mimeMessage ->
          val messageHelper = MimeMessageHelper(mimeMessage).apply {
            setFrom("convene");
            setTo(to);
            setSubject(subject)
          }

          val email = builder.build(subject, content)
          messageHelper.setText(email, true)
        }
        )
      } catch (e: MailException) {
        System.err.println(e.message)
      }
    }
  }

  fun sendInvoiceEmail(us: UserSubscription) {
    if (enabled) {
      try {
        sender.send({ mimeMessage ->
          val messageHelper = MimeMessageHelper(mimeMessage).apply {
            setFrom("convene")
            setTo(us.user?.email)
            setSubject("Invoice")
          }

          val email = builder.buildInvoice(us)
          messageHelper.setText(email, true)
        })
      } catch (e: MailException) {
        System.err.println(e.message)
      }
    }
  }

  fun sendVerificationEmail(user: User, url: String) {
    if (enabled) {
      try {
        sender.send({ mimeMessage ->
          val messageHelper = MimeMessageHelper(mimeMessage).apply {
            setFrom("convene");
            setTo(user.email);
            setSubject("Email verification");
          }

          val email = builder.buildVerification(user, url)
          messageHelper.setText(email, true)
        })
      } catch (e: MailException) {
        System.err.println(e.message)
      }
    }
  }

  fun sendWelcomeEmail(to: String) {

  }
}
