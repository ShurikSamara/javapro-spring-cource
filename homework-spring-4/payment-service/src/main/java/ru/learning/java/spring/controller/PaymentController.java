package ru.learning.java.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.learning.java.spring.dto.PaymentRequest;
import ru.learning.java.spring.dto.PaymentResponse;
import ru.learning.java.spring.model.Product;
import ru.learning.java.spring.service.PaymentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

  private final PaymentService paymentService;

  @Autowired
  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @PostMapping("/process")
  public ResponseEntity<PaymentResponse> processPayment(@RequestBody PaymentRequest request) {
    PaymentResponse response = paymentService.processPayment(request);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/client/{clientId}/products")
  public ResponseEntity<List<Product>> getClientProducts(@PathVariable Long clientId) {
    List<Product> products = paymentService.getClientProducts(clientId);
    return ResponseEntity.ok(products);
  }
}