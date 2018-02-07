package com.pancisin.bookster.events.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.pancisin.bookster.repository.ConferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.pancisin.bookster.events.OnPaymentEvent;
import com.pancisin.bookster.events.OnPaymentEvent.PaymentState;
import com.pancisin.bookster.model.Page;
import com.pancisin.bookster.model.enums.PageState;
import com.pancisin.bookster.repository.PageRepository;

@Component
public class PaymentListener implements ApplicationListener<OnPaymentEvent> {

  @Autowired
  private PageRepository pageRepository;

  @Autowired
  private ConferenceRepository conferenceRepository;

  @Override
  public void onApplicationEvent(OnPaymentEvent event) {
    if (event.getState() == PaymentState.SUCCESS) {
      Long user_id = event.getUs().getUser().getId();

      List<Page> pages = pageRepository.getByOwner(user_id);
      pages.addAll(conferenceRepository.getByOwner(user_id));

      List<Page> updatedPages = pages.stream()
        .filter(x -> x.getState() == PageState.BLOCKED)
        .peek(x -> x.setState(PageState.PUBLISHED)).collect(Collectors.toList());

      pageRepository.save(updatedPages);

      // Send approval email to the customer. Log transactions as well !
    } else if (event.getState() == PaymentState.ERROR) {
      // send warning error about unsuccessful transaction.
    }
  }
}
