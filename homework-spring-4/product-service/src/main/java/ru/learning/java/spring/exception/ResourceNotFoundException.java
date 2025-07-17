package ru.learning.java.spring.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseApiException {
  public ResourceNotFoundException(String message) {
    super(message, HttpStatus.NOT_FOUND, "Resource Not Found");
  }
}