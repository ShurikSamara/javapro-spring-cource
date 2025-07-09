package ru.learning.java.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.learning.java.spring.model.Payment;
import ru.learning.java.spring.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

  List<Payment> findByClientId(Long clientId);

  List<Payment> findByProductId(Long productId);

  List<Payment> findByStatus(PaymentStatus status);

  List<Payment> findByClientIdAndStatus(Long clientId, PaymentStatus status);

  List<Payment> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

  List<Payment> findByClientIdAndCreatedAtBetween(Long clientId, LocalDateTime startDate, LocalDateTime endDate);

  List<Payment> findByAmountGreaterThan(BigDecimal amount);

  List<Payment> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);

  long countByStatus(PaymentStatus status);

  long countByClientId(Long clientId);

  boolean existsByClientIdAndProductId(Long clientId, Long productId);

  Optional<Payment> findTopByClientIdOrderByCreatedAtDesc(Long clientId);

  List<Payment> findByClientIdOrderByCreatedAtDesc(Long clientId);

  @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.clientId = :clientId AND p.status = :status")
  BigDecimal getTotalAmountByClientIdAndStatus(@Param("clientId") Long clientId, @Param("status") PaymentStatus status);

  @Query("SELECT p.status, COUNT(p), SUM(p.amount) FROM Payment p GROUP BY p.status")
  List<Object[]> getPaymentStatistics();

  @Query("SELECT p FROM Payment p WHERE p.clientId = :clientId ORDER BY p.createdAt DESC")
  List<Payment> findPaymentsByClientIdWithPagination(@Param("clientId") Long clientId);

  @Query("SELECT p FROM Payment p WHERE p.status = :status AND p.createdAt < :dateTime")
  List<Payment> findStalePayments(@Param("status") PaymentStatus status, @Param("dateTime") LocalDateTime dateTime);

  @Query("SELECT p FROM Payment p WHERE p.clientId = :clientId AND p.productId = :productId AND p.amount = :amount AND p.status != 'FAILED'")
  List<Payment> findDuplicatePayments(@Param("clientId") Long clientId, @Param("productId") Long productId, @Param("amount") BigDecimal amount);
}