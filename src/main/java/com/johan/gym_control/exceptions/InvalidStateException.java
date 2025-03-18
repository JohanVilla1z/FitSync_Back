package com.johan.gym_control.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidStateException extends RuntimeException {
  public InvalidStateException(String message) {
    super(message);
  }
}