package com.pancisin.bookster.websocket.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.pancisin.bookster.websocket.services.ActiveUserService;

@Controller
public class ActiveUserController {
  
  @Autowired
  private ActiveUserService activeUserService;
  
  @MessageMapping("/activeUsers")
  public void activeUsers(Message<Object> message) {
    Principal user = message.getHeaders().get(SimpMessageHeaderAccessor.USER_HEADER, Principal.class);
    activeUserService.mark(user.getName());
  }
}