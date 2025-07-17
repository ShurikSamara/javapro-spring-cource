package ru.learning.java.spring.exception;

import org.springframework.http.HttpStatus;

public interface ApiException {
  HttpStatus getHttpStatus();
  String getErrorType();
}
