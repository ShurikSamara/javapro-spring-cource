package ru.learning.java.spring.exception;

import org.springframework.http.HttpStatus;

public class InsufficientFundsException extends BaseApiException {
  public InsufficientFundsException(String message) {
    super(message, HttpStatus.BAD_REQUEST, "Insufficient Funds");
  }
}