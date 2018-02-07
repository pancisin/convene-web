package com.pancisin.bookster.events.listeners;

import com.pancisin.bookster.events.OnServiceRequestEvent;
import com.pancisin.bookster.model.Service;
import com.pancisin.bookster.model.User;
import com.pancisin.bookster.services.EmailService;
import com.pancisin.bookster.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

public class ServiceRequestListener implements ApplicationListener<OnServiceRequestEvent> {

  @Autowired
  private EmailService emailService;

  @Autowired
  private NotificationService notificationService;

  @Override
  public void onApplicationEvent(OnServiceRequestEvent event) {
    Service service = event.getServiceRequest().getService();

    if (service != null && service.getPage() != null && service.getPage().getAdministrators() != null) {
      service.getPage().getAdministrators().forEach(admin -> {
        User user = admin.getUser();
        if (user != null) {
          emailService.sendSimpleMessage(user.getEmail(), "Service request", "");
          notificationService.notifyUser(user, "Service request", "");
        }
      });
    }
  }
}
