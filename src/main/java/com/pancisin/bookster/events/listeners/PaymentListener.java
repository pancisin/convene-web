package com.pancisin.bookster.events.listeners;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import com.pancisin.bookster.events.OnPaymentEvent;
import com.pancisin.bookster.events.OnPaymentEvent.PaymentState;
import com.pancisin.bookster.models.Conference;
import com.pancisin.bookster.models.Page;
import com.pancisin.bookster.models.enums.PageState;
import com.pancisin.bookster.repository.ConferenceRepository;
import com.pancisin.bookster.repository.PageRepository;

public class PaymentListener implements ApplicationListener<OnPaymentEvent> {

	@Autowired
	private PageRepository pageRepository;

	@Autowired
	private ConferenceRepository conferenceRepository;
	
	@Override
	public void onApplicationEvent(OnPaymentEvent event) {
		if (event.getState() == PaymentState.SUCCESS) {

			List<Page> updatedPages = new ArrayList<Page>();
			event.getUs().getUser().getOwningPages().stream().filter(x -> {
				return x.getState() == PageState.BLOCKED;
			}).forEach(x -> {
				x.setState(PageState.PUBLISHED);
				updatedPages.add(x);
			});
			
			List<Conference> updatedConferences = new ArrayList<Conference>();
			event.getUs().getUser().getOwningConferences().stream().filter(x -> {
				return x.getState() == PageState.BLOCKED;
			}).forEach(x -> {
				x.setState(PageState.PUBLISHED);
				updatedConferences.add(x);
			});
			
			pageRepository.save(updatedPages);
			conferenceRepository.save(updatedConferences);
			// Send approval email to the customer. Log transactions as well !
		} else if (event.getState() == PaymentState.ERROR) {
			// send warning error about unsuccessful transaction.
		}
	}
}
