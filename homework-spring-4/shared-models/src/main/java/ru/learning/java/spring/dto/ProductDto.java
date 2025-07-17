package ru.learning.java.spring.dto;

import java.math.BigDecimal;

public record ProductDto(
  Long id,
  String accountNumber,
  BigDecimal balance,
  String productType,
  Long clientId
) {}