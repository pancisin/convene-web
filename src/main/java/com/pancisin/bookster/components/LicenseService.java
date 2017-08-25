package com.pancisin.bookster.components;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pancisin.bookster.models.UserSubscription;
import com.pancisin.bookster.models.enums.PageState;
import com.pancisin.bookster.models.enums.SubscriptionState;
import com.pancisin.bookster.repository.UserSubscriptionRepository;

@Component
public class LicenseService {

	@Autowired
	private UserSubscriptionRepository usRepository;

	@Autowired
	private EmailService emailService;

	@Scheduled(cron = "0 0 3 * * *")
	// @Scheduled(fixedDelay = 10000)
	@Transactional
	public void checkLicenses() {
		List<UserSubscription> newSubs = new ArrayList<UserSubscription>();

		List<UserSubscription> expired = usRepository.findExpirations();

		expired.stream().forEach(s -> {
			if (s.getState() == SubscriptionState.ACTIVE) {
				s.setState(SubscriptionState.EXPIRED);
				newSubs.add(createNew(s));
			} else if (s.getState() == SubscriptionState.NEW) {
				s.setState(SubscriptionState.UNPAID);
				s.getUser().getOwningPages().stream().forEach(p -> p.setState(PageState.BLOCKED));
				s.getUser().getOwningConferences().stream().forEach(p -> p.setState(PageState.BLOCKED));
			}
		});

		usRepository.save(newSubs);
		newSubs.stream().forEach(us -> {
			emailService.sendInvoiceEmail(us);
		});

		usRepository.save(expired);
	}

	private UserSubscription createNew(UserSubscription previous) {
		UserSubscription subscription = new UserSubscription();
		subscription.setUser(previous.getUser());
		subscription.setSubscription(previous.getSubscription());
		subscription.setState(SubscriptionState.NEW);

		Calendar expiration = Calendar.getInstance();
		expiration.add(Calendar.MONTH, 1);
		subscription.setExpires(expiration);
		return subscription;
	}
}
