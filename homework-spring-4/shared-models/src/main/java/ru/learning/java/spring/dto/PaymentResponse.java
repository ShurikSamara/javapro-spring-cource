package ru.learning.java.spring.dto;

import java.math.BigDecimal;

public record PaymentResponse(
  Long paymentId,
  String status,
  BigDecimal amount,
  String message
) {}