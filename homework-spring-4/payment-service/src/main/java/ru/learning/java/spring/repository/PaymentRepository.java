package ru.learning.java.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.learning.java.spring.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
