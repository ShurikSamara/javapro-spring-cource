package ru.learning.java.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.learning.java.spring.model.Product;
import ru.learning.java.spring.service.ProductService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

  private final ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<Product> getProductById(@PathVariable Long id) {
    try {
      Optional<Product> product = productService.getProductById(id);
      return product.map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<Product>> getProductsByUserId(@PathVariable Long userId) {
    try {
      List<Product> products = productService.getProductsByUserId(userId);
      return ResponseEntity.ok(products);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping
  public ResponseEntity<List<Product>> getAllProducts() {
    List<Product> products = productService.getAllProducts();
    return ResponseEntity.ok(products);
  }

  @PostMapping
  public ResponseEntity<Product> createProduct(@RequestBody Product product) {
    try {
      Product createdProduct = productService.createProduct(product);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
    try {
      product.setId(id);
      Product updatedProduct = productService.updateProduct(product);
      return ResponseEntity.ok(updatedProduct);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
    try {
      productService.deleteProduct(id);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }
}