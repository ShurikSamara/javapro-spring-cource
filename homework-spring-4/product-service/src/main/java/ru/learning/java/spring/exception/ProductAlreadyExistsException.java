package ru.learning.java.spring.exception;

import org.springframework.http.HttpStatus;

public class ProductAlreadyExistsException extends BaseApiException {
  public ProductAlreadyExistsException(String message) {
    super(message, HttpStatus.CONFLICT, "Product Already Exists");
  }
}