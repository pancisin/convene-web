package com.pancisin.bookster.rest.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pancisin.bookster.events.OnPaymentEvent;
import com.pancisin.bookster.events.OnPaymentEvent.PaymentState;
import com.pancisin.bookster.model.UserSubscription;
import com.pancisin.bookster.models.enums.SubscriptionState;
import com.pancisin.bookster.repository.UserSubscriptionRepository;
import com.paylane.client.PayLaneClientBuilder;
import com.paylane.client.api.models.Card;
import com.paylane.client.api.models.CardSale;
import com.paylane.client.api.models.CardSaleResult;
import com.paylane.client.api.models.Customer;
import com.paylane.client.api.models.wrappers.CardSaleWrapper;

import retrofit2.Call;
import retrofit2.Response;

@RestController
@PreAuthorize("hasPermission(#license_id, 'license', '')")
@RequestMapping("/api/license/{license_id}")
public class LicenseController {

	@Autowired
	private UserSubscriptionRepository usRepository;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@GetMapping
	public ResponseEntity<?> getLicense(@PathVariable String license_id) {
		return ResponseEntity.ok(usRepository.findById(license_id));
	}

	@PostMapping("/payment")
	public ResponseEntity<?> payLicense(@PathVariable String license_id, @RequestBody Card card,
			HttpServletRequest request) throws Exception {
		UserSubscription us = usRepository.findById(license_id);
		if (us.getState() == SubscriptionState.ACTIVE || us.getState() == SubscriptionState.EXPIRED)
			throw new Exception("");

		CardSale sale = new CardSale((double) us.getSubscription().getPrice(), "EUR", us.getSubscription().getName());
		Customer customer = new Customer(us.getUser().getEmail(), request.getRemoteAddr(),
				us.getUser().getAddress().getPaylaneAddress());

		Call<CardSaleResult> call = PayLaneClientBuilder.createService("pancisin", "pru3cu6j")
				.cardsSale(new CardSaleWrapper(sale, customer, card));
		try {
			Response<CardSaleResult> result = call.execute();

			if (result.body().isSuccess()) {
				us.setState(SubscriptionState.ACTIVE);
				us.setIdSale(result.body().getIdSale());
				usRepository.save(us);

				eventPublisher.publishEvent(new OnPaymentEvent(us, PaymentState.SUCCESS));

				return ResponseEntity.ok(result);
			} else {
				eventPublisher.publishEvent(new OnPaymentEvent(us, PaymentState.ERROR));
				return new ResponseEntity<String>("Payment failed please check your card information.",
						HttpStatus.BAD_REQUEST);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return new ResponseEntity(HttpStatus.BAD_REQUEST);
	}
}
