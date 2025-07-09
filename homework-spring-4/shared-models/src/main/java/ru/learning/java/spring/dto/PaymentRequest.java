package ru.learning.java.spring.dto;

import java.math.BigDecimal;

public class PaymentRequest {
  private Long clientId;
  private Long productId;
  private BigDecimal amount;
  private String description;

  public PaymentRequest() {}

  public PaymentRequest(Long clientId, Long productId, BigDecimal amount, String description) {
    this.clientId = clientId;
    this.productId = productId;
    this.amount = amount;
    this.description = description;
  }

  public Long getClientId() { return clientId; }
  public void setClientId(Long clientId) { this.clientId = clientId; }

  public Long getProductId() { return productId; }
  public void setProductId(Long productId) { this.productId = productId; }

  public BigDecimal getAmount() { return amount; }
  public void setAmount(BigDecimal amount) { this.amount = amount; }

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
}