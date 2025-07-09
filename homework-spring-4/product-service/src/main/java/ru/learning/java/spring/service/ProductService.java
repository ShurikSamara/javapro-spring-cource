package ru.learning.java.spring.service;

import org.springframework.stereotype.Service;
import ru.learning.java.spring.exception.ProductAlreadyExistsException;
import ru.learning.java.spring.exception.ProductNotFoundException;
import ru.learning.java.spring.exception.ProductValidationException;
import ru.learning.java.spring.model.Product;
import ru.learning.java.spring.repository.ProductRepository;

import java.util.List;
import java.util.Optional;


@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Product updateProduct(Long id, Product product) {
    if (!productRepository.existsById(id)) {
      throw new ProductNotFoundException("Продукт с ID " + id + " не найден");
    }
    if (product.getAccountNumber() == null || product.getAccountNumber().isEmpty()) {
      throw new ProductValidationException("Номер счета обязателен");
    }
    return productRepository.save(product);
  }

  public void deleteProduct(Long id) {
    if (!productRepository.existsById(id)) {
      throw new ProductNotFoundException("Продукт с ID " + id + " не найден для удаления");
    }
    productRepository.deleteById(id);
  }

  public Product createProduct(Product product) {
    if (productRepository.existsByAccountNumber(product.getAccountNumber())) {
      throw new ProductAlreadyExistsException("Продукт с номером счета " + product.getAccountNumber() + " уже существует");
    }
    return productRepository.save(product);
  }

  public Optional<Product> getProductById(Long id) {
    if (id == null) {
      throw new IllegalArgumentException("ID продукта не может быть null");
    }
    return productRepository.findById(id);
  }

  public List<Product> getProductsByClientId(Long clientId) {
    if (clientId == null) {
      throw new IllegalArgumentException("ID пользователя не может быть null");
    }
    return productRepository.findByClientId(clientId);
  }

  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }
}