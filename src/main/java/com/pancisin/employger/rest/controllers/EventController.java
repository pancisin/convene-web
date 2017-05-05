package com.pancisin.employger.rest.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasPermission(#event_id, 'event', '')")
@RequestMapping("/api/event/{event_id}")
public class EventController {

}
