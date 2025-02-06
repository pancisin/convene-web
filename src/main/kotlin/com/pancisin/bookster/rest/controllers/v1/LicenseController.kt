package com.pancisin.bookster.rest.controllers.v1

import java.io.IOException

import javax.servlet.http.HttpServletRequest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import com.pancisin.bookster.events.OnPaymentEvent
import com.pancisin.bookster.events.OnPaymentEvent.PaymentState
import com.pancisin.bookster.model.UserSubscription
import com.pancisin.bookster.model.enums.SubscriptionState
import com.pancisin.bookster.repository.UserSubscriptionRepository
// import com.paylane.client.PayLaneClientBuilder
// import com.paylane.client.api.models.Card
// import com.paylane.client.api.models.CardSale
// import com.paylane.client.api.models.CardSaleResult
// import com.paylane.client.api.models.Customer
// import com.paylane.client.api.models.wrappers.CardSaleWrapper

// import retrofit2.Call
// import retrofit2.Response

@RestController
@PreAuthorize("hasPermission(#license_id, 'license', '')")
@RequestMapping("/api/v1/license/{license_id}")
class LicenseController {

  @Autowired
  lateinit var usRepository: UserSubscriptionRepository

  @Autowired
  lateinit var eventPublisher: ApplicationEventPublisher

  @GetMapping
  fun getLicense(@PathVariable license_id: String) = ResponseEntity.ok(usRepository.findById(license_id))

  @PostMapping("/payment")
  @Throws(Exception::class)
  fun payLicense(
    @PathVariable license_id: String,
    // @RequestBody card: Card,
    request: HttpServletRequest
  ): ResponseEntity<*> {

    // val us = usRepository.findById(license_id)
    // if (us.state === SubscriptionState.ACTIVE || us.state === SubscriptionState.EXPIRED)
    //   throw Exception("")

    // val sale = CardSale(us.subscription?.price?.toDouble(), "EUR", us.subscription?.prop)

    // us.user?.let { user ->
    //   val customer = Customer(user.email, request.remoteAddr, user.address.getPaylaneAddress())
    //   val call = PayLaneClientBuilder.createService("pancisin", "pru3cu6j").cardsSale(CardSaleWrapper(sale, customer, card))

    //   try {
    //     val result = call.execute()

    //     result.body()?.let { body ->
    //       if (body.isSuccess) {
    //         us.apply {
    //           state = SubscriptionState.ACTIVE;
    //           idSale = body.idSale
    //         }

    //         usRepository.save(us)
    //         eventPublisher.publishEvent(OnPaymentEvent(us, PaymentState.SUCCESS))
    //         return ResponseEntity.ok(result)
    //       } else {
    //         eventPublisher.publishEvent(OnPaymentEvent(us, PaymentState.ERROR))
    //         return ResponseEntity("Payment failed please check your card information.", HttpStatus.BAD_REQUEST)
    //       }
    //     }
    //   } catch (e: IOException) {
    //     e.printStackTrace()
    //   }
    // }

    return ResponseEntity<String>(HttpStatus.BAD_REQUEST)
  }
}
