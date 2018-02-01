package com.pancisin.bookster.events;

import com.pancisin.bookster.model.Service;
import com.pancisin.bookster.model.ServiceRequest;
import org.springframework.context.ApplicationEvent;

public class OnServiceRequestEvent extends ApplicationEvent {

  private ServiceRequest request;

  public OnServiceRequestEvent(ServiceRequest request) {
    super(request);
    this.request = request;
  }

  public ServiceRequest getServiceRequest() {
    return request;
  }
}
