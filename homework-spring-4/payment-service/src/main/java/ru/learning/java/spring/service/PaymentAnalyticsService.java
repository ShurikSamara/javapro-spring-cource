package ru.learning.java.spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.learning.java.spring.config.PaymentServiceProperties;
import ru.learning.java.spring.model.Payment;
import ru.learning.java.spring.model.PaymentStatus;
import ru.learning.java.spring.repository.PaymentRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для аналитики платежей и отчётности
 */
@Service
public class PaymentAnalyticsService {
    private static final Logger log = LoggerFactory.getLogger(PaymentAnalyticsService.class);
    private final PaymentRepository paymentRepository;
    private final PaymentServiceProperties paymentServiceProperties;

    public PaymentAnalyticsService(PaymentRepository paymentRepository, PaymentServiceProperties paymentServiceProperties) {
        this.paymentRepository = paymentRepository;
        this.paymentServiceProperties = paymentServiceProperties;
    }

    /**
     * Получить платежи по статусу
     */
    public List<Payment> getPaymentsByStatus(PaymentStatus status) {
        log.debug("Fetching payments with status: {}", status);
        return paymentRepository.findByStatus(status);
    }

    /**
     * Получить платежи за период
     */
    public List<Payment> getPaymentsByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Fetching payments between {} and {}", startDate, endDate);
        return paymentRepository.findByCreatedAtBetween(startDate, endDate);
    }

    /**
     * Получить платежи конкретного клиента за период
     */
    public List<Payment> getClientPaymentsByPeriod(Long clientId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Fetching payments for client {} between {} and {}", clientId, startDate, endDate);
        return paymentRepository.findByClientIdAndCreatedAtBetween(clientId, startDate, endDate);
    }

    /**
     * Поиск больших платежей (с указанием нижнего порога)
     */
    public List<Payment> getLargePayments(BigDecimal minimumAmount) {
        log.debug("Fetching payments with amount greater than {}", minimumAmount);
        return paymentRepository.findByAmountGreaterThan(minimumAmount);
    }

    /**
     * Поиск платежей в диапазоне
     */
    public List<Payment> getPaymentsByAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        log.debug("Fetching payments with amount between {} and {}", minAmount, maxAmount);
        return paymentRepository.findByAmountBetween(minAmount, maxAmount);
    }

    /**
     * Count payments by status
     */
    public long countPaymentsByStatus(PaymentStatus status) {
        log.debug("Counting payments with status: {}", status);
        return paymentRepository.countByStatus(status);
    }

    /**
     * Количество платежей конкретного клиента
     */
    public long countClientPayments(Long clientId) {
        log.debug("Counting payments for client {}", clientId);
        return paymentRepository.countByClientId(clientId);
    }

    /**
     * Проверка, оплачен ли продукт
     */
    public boolean hasClientPaidForProduct(Long clientId, Long productId) {
        log.debug("Checking if client {} has paid for product {}", clientId, productId);
        return paymentRepository.existsByClientIdAndProductId(clientId, productId);
    }

    /**
     * Последний платёж клиента
     */
    public Optional<Payment> getLastClientPayment(Long clientId) {
        log.debug("Fetching last payment for client {}", clientId);
        return paymentRepository.findTopByClientIdOrderByCreatedAtDesc(clientId);
    }

    /**
     * Получить общую сумму платежей по конкретному клиенту и их статусу
     */
    public BigDecimal getTotalAmountByClientAndStatus(Long clientId, PaymentStatus status) {
        log.debug("Calculating total amount for client {} with status {}", clientId, status);
        BigDecimal total = paymentRepository.getTotalAmountByClientIdAndStatus(clientId, status);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Поиск просроченных платежей
     */
    public List<Payment> findStalePayments(PaymentStatus status, LocalDateTime cutoffDate) {
        log.debug("Finding stale payments with status {} before {}", status, cutoffDate);
        return paymentRepository.findStalePayments(status, cutoffDate);
    }

    /**
     * Поиск дублирующих платежей
     */
    public List<Payment> findDuplicatePayments(Long clientId, Long productId, BigDecimal amount) {
        log.debug("Finding duplicate payments for client {}, product {}, amount {}", clientId, productId, amount);
        return paymentRepository.findDuplicatePayments(clientId, productId, amount);
    }

    /**
     * Получить общую сумму успешных платежей от клиента
     */
    public BigDecimal getClientTotalSuccessfulPayments(Long clientId) {
        log.debug("Calculating total successful payments for client {}", clientId);
        return getTotalAmountByClientAndStatus(clientId, PaymentStatus.COMPLETED);
    }

    /**
     * Проверить, может ли клиент совершить платёж, основываясь на недавней истории платежей.
     *
     * @param clientId the client ID
     * @return true if the client can make a payment, false otherwise
     */
    public boolean canClientMakePayment(Long clientId) {
        log.debug("Checking if client {} can make payment", clientId);

        // Запрашиваем платежи за последние 24 часа
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        List<Payment> recentFailedPayments = paymentRepository.findByClientIdAndCreatedAtBetween(
                clientId, yesterday, LocalDateTime.now())
            .stream()
            .filter(p -> p.getStatus() == PaymentStatus.FAILED)
            .toList();

        int maxFailedPayments = paymentServiceProperties.getMaxFailedPayments();
        log.debug("Client {} has {} failed payments in the last 24 hours (max allowed: {})",
            clientId, recentFailedPayments.size(), maxFailedPayments);

        return recentFailedPayments.size() < maxFailedPayments;
    }
}
