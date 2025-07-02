package ru.learning.java.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.learning.java.spring.dao.ProductDao;
import ru.learning.java.spring.model.Product;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

  private final ProductDao productDao;

  @Autowired
  public ProductService(ProductDao productDao) {
    this.productDao = productDao;
  }

  public Product createProduct(Product product) {
    if (product == null) {
      throw new IllegalArgumentException("Продукт не может быть null");
    }
    if (product.getAccountNumber() == null || product.getAccountNumber().trim().isEmpty()) {
      throw new IllegalArgumentException("Номер счета не может быть пустым");
    }
    if (product.getBalance() == null || product.getBalance() < 0) {
      throw new IllegalArgumentException("Баланс не может быть отрицательным");
    }
    if (product.getProductType() == null) {
      throw new IllegalArgumentException("Тип продукта должен быть указан");
    }
    if (product.getUserId() == null || product.getUserId() <= 0) {
      throw new IllegalArgumentException("ID пользователя должен быть положительным числом");
    }

    return productDao.save(product);
  }

  public Optional<Product> getProductById(Long id) {
    if (id == null || id <= 0) {
      throw new IllegalArgumentException("ID должен быть положительным числом");
    }
    return productDao.findById(id);
  }

  public List<Product> getProductsByUserId(Long userId) {
    if (userId == null || userId <= 0) {
      throw new IllegalArgumentException("ID пользователя должен быть положительным числом");
    }
    return productDao.findByUserId(userId);
  }

  public List<Product> getAllProducts() {
    return productDao.findAll();
  }
}