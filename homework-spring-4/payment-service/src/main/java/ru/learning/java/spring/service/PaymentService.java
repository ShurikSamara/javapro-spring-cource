package ru.learning.java.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.learning.java.spring.dto.PaymentRequest;
import ru.learning.java.spring.dto.PaymentResponse;
import ru.learning.java.spring.exception.InsufficientFundsException;
import ru.learning.java.spring.exception.PaymentException;
import ru.learning.java.spring.exception.ProductNotFoundException;
import ru.learning.java.spring.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PaymentService {

  private final RestTemplate restTemplate;
  private final AtomicLong paymentIdCounter = new AtomicLong(1);

  @Value("${product.service.url:http://localhost:8080}")
  private String productServiceUrl;

  @Autowired
  public PaymentService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public List<Product> getUserProducts(Long userId) {
    try {
      String url = productServiceUrl + "/api/products/user/" + userId;
      ResponseEntity<List<Product>> response = restTemplate.exchange(
        url,
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<>() {}
      );
      return response.getBody();
    } catch (RestClientException e) {
      throw new PaymentException("Ошибка при получении продуктов пользователя из сервиса продуктов", e);
    }
  }

  public PaymentResponse processPayment(PaymentRequest request) {
    // Валидация запроса
    validatePaymentRequest(request);

    // Получение продукта
    Product product = getProductById(request.getProductId());

    // Проверка принадлежности продукта пользователю
    if (!product.getUserId().equals(request.getUserId())) {
      throw new ProductNotFoundException("Продукт не принадлежит указанному пользователю");
    }

    // Проверка достаточности средств
    if (product.getBalance().compareTo(request.getAmount()) < 0) {
      throw new InsufficientFundsException("Недостаточно средств на счете. Доступно: " +
        product.getBalance() + ", требуется: " + request.getAmount());
    }

    // Обновление баланса продукта
    BigDecimal newBalance = product.getBalance().subtract(request.getAmount());
    product.setBalance(newBalance);

    // Обновление продукта в сервисе продуктов
    updateProduct(product);

    // Создание успешного ответа
    Long paymentId = paymentIdCounter.getAndIncrement();
    return new PaymentResponse(paymentId, "SUCCESS", request.getAmount(),
      "Платеж успешно обработан");
  }

  private void validatePaymentRequest(PaymentRequest request) {
    if (request.getUserId() == null) {
      throw new IllegalArgumentException("ID пользователя не может быть null");
    }
    if (request.getProductId() == null) {
      throw new IllegalArgumentException("ID продукта не может быть null");
    }
    if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Сумма платежа должна быть больше нуля");
    }
  }

  private Product getProductById(Long productId) {
    try {
      String url = productServiceUrl + "/api/products/" + productId;
      ResponseEntity<Product> response = restTemplate.getForEntity(url, Product.class);

      if (response.getBody() == null) {
        throw new ProductNotFoundException("Продукт с ID " + productId + " не найден");
      }

      return response.getBody();
    } catch (RestClientException e) {
      throw new PaymentException("Ошибка при получении продукта из сервиса продуктов", e);
    }
  }

  private void updateProduct(Product product) {
    try {
      String url = productServiceUrl + "/api/products/" + product.getId();
      restTemplate.put(url, product);
    } catch (RestClientException e) {
      throw new PaymentException("Ошибка при обновлении продукта в сервисе продуктов", e);
    }
  }
}