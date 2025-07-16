package ru.learning.java.spring.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalPaymentExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalPaymentExceptionHandler.class);

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleProductNotFoundException(
    ProductNotFoundException e, WebRequest request) {
    logger.warn("Продукт не найден: {}", e.getMessage());

    Map<String, Object> errorResponse = createErrorResponse(
      HttpStatus.NOT_FOUND, "Product Not Found", e.getMessage(), request);

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(ProductValidationException.class)
  public ResponseEntity<Map<String, Object>> handleProductValidationException(
    ProductValidationException e, WebRequest request) {
    logger.warn("Ошибка валидации продукта: {}", e.getMessage());

    Map<String, Object> errorResponse = createErrorResponse(
      HttpStatus.BAD_REQUEST, "Product Validation Error", e.getMessage(), request);

    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler(ProductAlreadyExistsException.class)
  public ResponseEntity<Map<String, Object>> handleProductAlreadyExistsException(
    ProductAlreadyExistsException e, WebRequest request) {
    logger.warn("Продукт уже существует: {}", e.getMessage());

    Map<String, Object> errorResponse = createErrorResponse(
      HttpStatus.CONFLICT, "Product Already Exists", e.getMessage(), request);

    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
  }

  // Обработка валидации Jakarta Bean Validation
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationExceptions(
    MethodArgumentNotValidException e, WebRequest request) {
    logger.warn("Ошибка валидации: {}", e.getMessage());

    Map<String, String> errors = new HashMap<>();
    e.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    Map<String, Object> errorResponse = createErrorResponse(
      HttpStatus.BAD_REQUEST, "Validation Failed", "Ошибка валидации полей", request);
    errorResponse.put("fieldErrors", errors);

    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
    IllegalArgumentException e, WebRequest request) {
    logger.warn("Неверный запрос: {}", e.getMessage());

    Map<String, Object> errorResponse = createErrorResponse(
      HttpStatus.BAD_REQUEST, "Bad Request", e.getMessage(), request);

    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalStateException(
    IllegalStateException e, WebRequest request) {
    logger.warn("Конфликт состояния: {}", e.getMessage());

    Map<String, Object> errorResponse = createErrorResponse(
      HttpStatus.CONFLICT, "Conflict", e.getMessage(), request);

    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(
    ResourceNotFoundException e, WebRequest request) {
    logger.warn("Ресурс не найден: {}", e.getMessage());

    Map<String, Object> errorResponse = createErrorResponse(
      HttpStatus.NOT_FOUND, "Not Found", e.getMessage(), request);

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }


  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Map<String, Object>> handleRuntimeException(
    RuntimeException e, WebRequest request) {
    logger.error("Внутренняя ошибка сервера: {}", e.getMessage(), e);

    Map<String, Object> errorResponse = createErrorResponse(
      HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
      "Произошла внутренняя ошибка сервера", request);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleGeneralException(
    Exception e, WebRequest request) {
    logger.error("Неожиданная ошибка: {}", e.getMessage(), e);

    Map<String, Object> errorResponse = createErrorResponse(
      HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
      "Произошла неожиданная ошибка", request);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  // Вот так создать отчёт об ошибке
  private Map<String, Object> createErrorResponse(HttpStatus status, String error,
                                                  String message, WebRequest request) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("timestamp", LocalDateTime.now());
    errorResponse.put("status", status.value());
    errorResponse.put("error", error);
    errorResponse.put("message", message);
    errorResponse.put("path", request.getDescription(false).replace("uri=", ""));
    return errorResponse;
  }
}
