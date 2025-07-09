package ru.learning.java.spring.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.learning.java.spring.dto.PaymentRequest;
import ru.learning.java.spring.dto.PaymentResponse;
import ru.learning.java.spring.exception.InsufficientFundsException;
import ru.learning.java.spring.exception.PaymentProcessingException;
import ru.learning.java.spring.model.Payment;
import ru.learning.java.spring.model.PaymentStatus;
import ru.learning.java.spring.model.Product;
import ru.learning.java.spring.repository.PaymentRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {
  private final PaymentRepository paymentRepository;
  private final ProductServiceClient productServiceClient;
  private final AccountService accountService;

  public PaymentService(PaymentRepository paymentRepository,
                        ProductServiceClient productServiceClient,
                        AccountService accountService) {
    this.paymentRepository = paymentRepository;
    this.productServiceClient = productServiceClient;
    this.accountService = accountService;
  }

  public List<Product> getUserProducts(Long userId) {
    return productServiceClient.getProductsByUserId(userId);
  }

  @Transactional
  public PaymentResponse processPayment(PaymentRequest request) {
    try {
      Product product = productServiceClient.getProductById(request.getProductId());

      if (!accountService.hasEnoughBalance(request.getUserId(), product.getPrice())) {
        throw new InsufficientFundsException("Insufficient funds for payment");
      }

      Payment payment = new Payment();
      payment.setClientId(request.getUserId());
      payment.setProductId(request.getProductId());
      payment.setAmount(product.getPrice());
      payment.setStatus(PaymentStatus.PENDING);
      payment.setCreatedAt(LocalDateTime.now());

      payment = paymentRepository.save(payment);

      try {
        accountService.debitAccount(request.getUserId(), product.getPrice());

        payment.setStatus(PaymentStatus.COMPLETED);
        payment = paymentRepository.save(payment);

        return new PaymentResponse(payment.getId(), "COMPLETED", payment.getAmount(), "Payment processed successfully");
      } catch (Exception e) {
        payment.setStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);
        throw new PaymentProcessingException("Payment processing failed");
      }
    } catch (Exception e) {
      throw e;
    }
  }
}
