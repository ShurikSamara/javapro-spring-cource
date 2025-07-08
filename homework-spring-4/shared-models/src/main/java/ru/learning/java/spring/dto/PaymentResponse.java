package ru.learning.java.spring.dto;

import java.math.BigDecimal;

public class PaymentResponse {
  private Long paymentId;
  private String status;
  private BigDecimal amount;
  private String message;

  public PaymentResponse() {}

  public PaymentResponse(Long paymentId, String status, BigDecimal amount, String message) {
    this.paymentId = paymentId;
    this.status = status;
    this.amount = amount;
    this.message = message;
  }

  public Long getPaymentId() { return paymentId; }
  public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  public BigDecimal getAmount() { return amount; }
  public void setAmount(BigDecimal amount) { this.amount = amount; }

  public String getMessage() { return message; }
  public void setMessage(String message) { this.message = message; }
}