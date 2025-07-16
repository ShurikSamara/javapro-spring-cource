package ru.learning.java.spring.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalPaymentExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalPaymentExceptionHandler.class);

  @ExceptionHandler(InsufficientFundsException.class)
  public ResponseEntity<Map<String, Object>> handleInsufficientFundsException(InsufficientFundsException e, WebRequest request) {
    logger.warn("Недостаточно средств: {}", e.getMessage());

    Map<String, Object> errorResponse = createErrorResponse(HttpStatus.BAD_REQUEST, "Insufficient Funds", e.getMessage(), request);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(PaymentProcessingException.class)
  public ResponseEntity<Map<String, Object>> handlePaymentProcessingException(PaymentProcessingException e, WebRequest request) {
    logger.error("Ошибка обработки платежа: {}", e.getMessage());

    Map<String, Object> errorResponse = createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Payment Processing Error", e.getMessage(), request);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  @ExceptionHandler(PaymentException.class)
  public ResponseEntity<Map<String, Object>> handlePaymentException(PaymentException e, WebRequest request) {
    logger.error("Ошибка платежа: {}", e.getMessage());

    Map<String, Object> errorResponse = createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Payment Error", e.getMessage(), request);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleProductNotFoundException(ProductNotFoundException e, WebRequest request) {
    logger.warn("Продукт не найден: {}", e.getMessage());

    Map<String, Object> errorResponse = createErrorResponse(HttpStatus.NOT_FOUND, "Product Not Found", e.getMessage(), request);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(ProductServiceException.class)
  public ResponseEntity<Map<String, Object>> handleProductServiceException(ProductServiceException e, WebRequest request) {
    logger.error("Ошибка сервиса продуктов: {}", e.getMessage());

    Map<String, Object> errorResponse = createErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, "Product Service Error", e.getMessage(), request);
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
  }

  private Map<String, Object> createErrorResponse(HttpStatus status, String error, String message, WebRequest request) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("timestamp", LocalDateTime.now());
    errorResponse.put("status", status.value());
    errorResponse.put("error", error);
    errorResponse.put("message", message);
    errorResponse.put("path", request.getDescription(false).replace("uri=", ""));
    return errorResponse;
  }
}