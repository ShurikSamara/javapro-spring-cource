package ru.learning.java.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.learning.java.spring.model.Payment;
import ru.learning.java.spring.model.PaymentStatus;
import ru.learning.java.spring.service.PaymentAnalyticsService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/analytics")
public class PaymentAnalyticsController {

  private final PaymentAnalyticsService analyticsService;

  @Autowired
  public PaymentAnalyticsController(PaymentAnalyticsService analyticsService) {
    this.analyticsService = analyticsService;
  }

  /**
   * Получить платежи по статусу
   */
  @GetMapping("/payments/status/{status}")
  public List<Payment> getPaymentsByStatus(@PathVariable PaymentStatus status) {
    return analyticsService.getPaymentsByStatus(status);
  }

  /**
   * Получить платежи за период
   */
  @GetMapping("/payments/period")
  public List<Payment> getPaymentsByPeriod(
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
    return analyticsService.getPaymentsByPeriod(startDate, endDate);
  }

  /**
   * Получить платежи конкретного клиента за период
   */
  @GetMapping("/payments/client/{clientId}/period")
  public List<Payment> getClientPaymentsByPeriod(
    @PathVariable Long clientId,
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
    return analyticsService.getClientPaymentsByPeriod(clientId, startDate, endDate);
  }

  /**
   * Получить большие платежи (с указанием нижнего порога)
   */
  @GetMapping("/payments/large")
  public List<Payment> getLargePayments(@RequestParam BigDecimal minimumAmount) {
    return analyticsService.getLargePayments(minimumAmount);
  }

  /**
   * Получить платежи в диапазоне сумм
   */
  @GetMapping("/payments/amount-range")
  public List<Payment> getPaymentsByAmountRange(
    @RequestParam BigDecimal minAmount,
    @RequestParam BigDecimal maxAmount) {
    return analyticsService.getPaymentsByAmountRange(minAmount, maxAmount);
  }

  /**
   * Количество платежей конкретного клиента
   */
  @GetMapping("/payments/client/{clientId}/count")
  public Map<String, Long> countClientPayments(@PathVariable Long clientId) {
    long count = analyticsService.countClientPayments(clientId);
    return Map.of("clientId", clientId, "paymentCount", count);
  }

  /**
   * Проверить, оплачен ли продукт клиентом
   */
  @GetMapping("/payments/client/{clientId}/product/{productId}/exists")
  public Map<String, Boolean> hasClientPaidForProduct(
    @PathVariable Long clientId,
    @PathVariable Long productId) {
    boolean hasPaid = analyticsService.hasClientPaidForProduct(clientId, productId);
    return Map.of("hasPaid", hasPaid);
  }

  /**
   * Получить общую сумму платежей по конкретному клиенту и их статусу
   */
  @GetMapping("/payments/client/{clientId}/total")
  public Map<String, Object> getTotalAmountByClientAndStatus(
    @PathVariable Long clientId,
    @RequestParam PaymentStatus status) {
    BigDecimal totalAmount = analyticsService.getTotalAmountByClientAndStatus(clientId, status);
    return Map.of(
      "clientId", clientId,
      "status", status,
      "totalAmount", totalAmount
    );
  }

  /**
   * Получить общую сумму успешных платежей от клиента
   */
  @GetMapping("/payments/client/{clientId}/successful-total")
  public Map<String, Object> getClientTotalSuccessfulPayments(@PathVariable Long clientId) {
    BigDecimal totalAmount = analyticsService.getClientTotalSuccessfulPayments(clientId);
    return Map.of(
      "clientId", clientId,
      "totalSuccessfulPayments", totalAmount
    );
  }

  /**
   * Найти просроченные платежи
   */
  @GetMapping("/payments/stale")
  public List<Payment> findStalePayments(
    @RequestParam PaymentStatus status,
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime cutoffDate) {
    return analyticsService.findStalePayments(status, cutoffDate);
  }

  /**
   * Найти дублирующие платежи
   */
  @GetMapping("/payments/duplicates")
  public List<Payment> findDuplicatePayments(
    @RequestParam Long clientId,
    @RequestParam Long productId,
    @RequestParam BigDecimal amount) {
    return analyticsService.findDuplicatePayments(clientId, productId, amount);
  }

  /**
   * Проверить, может ли клиент совершить платёж
   */
  @GetMapping("/payments/client/{clientId}/can-pay")
  public Map<String, Boolean> canClientMakePayment(@PathVariable Long clientId) {
    boolean canPay = analyticsService.canClientMakePayment(clientId);
    return Map.of("canMakePayment", canPay);
  }
}