package ru.learning.java.spring.exception;

public class PaymentProcessingException extends RuntimeException{
  public PaymentProcessingException(String message, Throwable cause) {
    super(message, cause);
  }
}