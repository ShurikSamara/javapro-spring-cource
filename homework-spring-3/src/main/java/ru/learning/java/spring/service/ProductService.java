package ru.learning.java.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.learning.java.spring.model.Product;
import ru.learning.java.spring.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  @Autowired
  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Product createProduct(Product product) {
    if (product == null) {
      throw new IllegalArgumentException("Продукт не может быть null");
    }
    if (product.getAccountNumber() == null || product.getAccountNumber().trim().isEmpty()) {
      throw new IllegalArgumentException("Номер счета не может быть пустым");
    }
    if ((product.getBalance() == null) || (product.getBalance().compareTo(BigDecimal.ZERO) < 0)) {
      throw new IllegalArgumentException("Баланс не может быть отрицательным");
    }
    if (product.getProductType() == null) {
      throw new IllegalArgumentException("Тип продукта должен быть указан");
    }
    if (product.getUserId() == null || product.getUserId() <= 0) {
      throw new IllegalArgumentException("ID пользователя должен быть положительным числом");
    }

    return productRepository.save(product);
  }

  public Optional<Product> getProductById(Long id) {
    if (id == null || id <= 0) {
      throw new IllegalArgumentException("ID должен быть положительным числом");
    }
    return productRepository.findById(id);
  }

  public List<Product> getProductsByUserId(Long userId) {
    if (userId == null || userId <= 0) {
      throw new IllegalArgumentException("ID пользователя должен быть положительным числом");
    }
    return productRepository.findByUserId(userId);
  }

  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }

  public Product updateProduct(Product product) {
    if (product == null || product.getId() == null) {
      throw new IllegalArgumentException("Продукт и его ID не могут быть null");
    }
    if (!productRepository.existsById(product.getId())) {
      throw new IllegalArgumentException("Продукт с ID " + product.getId() + " не найден");
    }
    return productRepository.save(product);
  }

  public void deleteProduct(Long id) {
    if (id == null || id <= 0) {
      throw new IllegalArgumentException("ID должен быть положительным числом");
    }
    if (!productRepository.existsById(id)) {
      throw new IllegalArgumentException("Продукт с ID " + id + " не найден");
    }
    productRepository.deleteById(id);
  }
}