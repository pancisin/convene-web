package com.pancisin.employger.security.exceptions;

import org.springframework.security.core.AuthenticationException;

public class JwtTokenMissingException extends AuthenticationException {
  public JwtTokenMissingException(String msg) {
    super(msg);
  }
}