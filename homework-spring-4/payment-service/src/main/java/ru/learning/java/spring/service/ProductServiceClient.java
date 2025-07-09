package ru.learning.java.spring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.learning.java.spring.exception.ProductNotFoundException;
import ru.learning.java.spring.model.Product;

import java.util.List;

@Service
public class ProductServiceClient {
  private final RestTemplate restTemplate;

  @Value("${product-service.url:http://localhost:8080}")
  private String productServiceUrl;

  public ProductServiceClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public List<Product> getProductsByUserId(Long userId) {
    String url = productServiceUrl + "/api/v1/products/user/" + userId;
    ResponseEntity<List<Product>> response = restTemplate.exchange(
      url,
      HttpMethod.GET,
      null,
      new ParameterizedTypeReference<>() {}
    );
    return response.getBody();
  }

  public Product getProductById(Long productId) {
    String url = productServiceUrl + "/api/v1/products/" + productId;
    try {
      return restTemplate.getForObject(url, Product.class);
    } catch (HttpClientErrorException.NotFound e) {
      throw new ProductNotFoundException("Product not found with id: " + productId);
    }
  }
}
