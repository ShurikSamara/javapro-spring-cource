package ru.learning.java.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.learning.java.spring.exception.ResourceNotFoundException;
import ru.learning.java.spring.model.Product;
import ru.learning.java.spring.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

  private final ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping("/{id}")
  public Product getProductById(@PathVariable("id") Long id) {
    if (id == null) {
      throw new IllegalArgumentException("ID продукта не может быть null");
    }
    return productService.getProductById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Продукт с ID " + id + " не найден"));
  }

  @GetMapping("/client/{clientId}")
  public ResponseEntity<List<Product>> getProductsByClientId(@PathVariable("clientId") Long clientId) {
    List<Product> products = productService.getProductsByClientId(clientId);
    return ResponseEntity.ok(products);
  }

  @GetMapping
  public List<Product> getAllProducts() {
    return productService.getAllProducts();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Product createProduct(@RequestBody Product product) {
    return productService.createProduct(product);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id, @RequestBody Product product) {
    if (product.getId() != null && !product.getId().equals(id)) {
      throw new IllegalArgumentException("ID в пути и в теле запроса не совпадают");
    }
    product.setId(id);
    Product updatedProduct = productService.updateProduct(id, product);
    return ResponseEntity.ok(updatedProduct);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
    productService.deleteProduct(id);
    return ResponseEntity.noContent().build();
  }
}