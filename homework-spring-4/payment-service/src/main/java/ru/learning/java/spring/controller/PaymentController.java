package ru.learning.java.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.learning.java.spring.dto.PaymentRequest;
import ru.learning.java.spring.dto.PaymentResponse;
import ru.learning.java.spring.exception.PaymentException;
import ru.learning.java.spring.exception.ProductNotFoundException;
import ru.learning.java.spring.exception.InsufficientFundsException;
import ru.learning.java.spring.model.Product;
import ru.learning.java.spring.service.PaymentService;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

  private final PaymentService paymentService;

  @Autowired
  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @PostMapping("/process")
  public ResponseEntity<PaymentResponse> processPayment(@RequestBody PaymentRequest request) {
    try {
      PaymentResponse response = paymentService.processPayment(request);
      return ResponseEntity.ok(response);
    } catch (ProductNotFoundException e) {
      PaymentResponse errorResponse = new PaymentResponse(null, "FAILED", request.getAmount(),
        "Продукт не найден: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    } catch (InsufficientFundsException e) {
      PaymentResponse errorResponse = new PaymentResponse(null, "FAILED", request.getAmount(),
        "Недостаточно средств: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    } catch (PaymentException e) {
      PaymentResponse errorResponse = new PaymentResponse(null, "FAILED", request.getAmount(),
        "Ошибка платежа: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    } catch (Exception e) {
      PaymentResponse errorResponse = new PaymentResponse(null, "FAILED", request.getAmount(),
        "Внутренняя ошибка сервера");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
  }

  @GetMapping("/user/{userId}/products")
  public ResponseEntity<List<Product>> getUserProducts(@PathVariable Long userId) {
    try {
      List<Product> products = paymentService.getUserProducts(userId);
      return ResponseEntity.ok(products);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}