package ru.learning.java.spring.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseApiException extends RuntimeException implements ApiException {
  private final HttpStatus httpStatus;
  private final String errorType;

  protected BaseApiException(String message, HttpStatus httpStatus, String errorType) {
    super(message);
    this.httpStatus = httpStatus;
    this.errorType = errorType;
  }

  protected BaseApiException(String message, Throwable cause, HttpStatus httpStatus, String errorType) {
    super(message, cause);
    this.httpStatus = httpStatus;
    this.errorType = errorType;
  }

  @Override
  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  @Override
  public String getErrorType() {
    return errorType;
  }
}