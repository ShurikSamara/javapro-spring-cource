package ru.learning.java.spring.exception;

import org.springframework.http.HttpStatus;

public class ProductServiceException extends BaseApiException {
  public ProductServiceException(String message, Throwable cause) {
    super(message, cause, HttpStatus.SERVICE_UNAVAILABLE, "Product Service Error");
  }
}