package ru.learning.java.spring.dto;

import java.math.BigDecimal;

public class PaymentRequest {
  private Long userId;
  private Long productId;
  private BigDecimal amount;
  private String description;

  public PaymentRequest() {}

  public PaymentRequest(Long userId, Long productId, BigDecimal amount, String description) {
    this.userId = userId;
    this.productId = productId;
    this.amount = amount;
    this.description = description;
  }

  public Long getUserId() { return userId; }
  public void setUserId(Long userId) { this.userId = userId; }

  public Long getProductId() { return productId; }
  public void setProductId(Long productId) { this.productId = productId; }

  public BigDecimal getAmount() { return amount; }
  public void setAmount(BigDecimal amount) { this.amount = amount; }

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
}