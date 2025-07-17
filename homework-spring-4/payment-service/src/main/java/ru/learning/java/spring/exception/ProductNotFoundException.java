package ru.learning.java.spring.exception;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends BaseApiException {
  public ProductNotFoundException(String message) {
    super(message, HttpStatus.NOT_FOUND, "Product Not Found");
  }
}