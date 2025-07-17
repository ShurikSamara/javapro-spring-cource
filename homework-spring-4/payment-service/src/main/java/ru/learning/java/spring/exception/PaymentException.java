package ru.learning.java.spring.exception;

import org.springframework.http.HttpStatus;

public class PaymentException extends BasePaymentException {
  public PaymentException(String message) {
    super(message, HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка платежа");
  }

  public PaymentException(String message, Throwable cause) {
    super(message, cause, HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка платежа");
  }
}