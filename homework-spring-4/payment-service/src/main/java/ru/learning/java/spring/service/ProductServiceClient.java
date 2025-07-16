package ru.learning.java.spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.learning.java.spring.dto.ProductDto;
import ru.learning.java.spring.exception.ProductNotFoundException;
import ru.learning.java.spring.exception.ProductServiceException;

import java.util.Collections;
import java.util.List;

@Service
public class ProductServiceClient {
  private static final Logger log = LoggerFactory.getLogger(ProductServiceClient.class);
  private final RestTemplate restTemplate;

  @Value("${product-service.url:http://localhost:8081}")
  private String productServiceUrl;

  public ProductServiceClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  /**
   * Возвращает все продкуты для определенного клиента
   *
   * @param clientId the ID of the client
   * @return list of products belonging to the client
   * @throws ProductServiceException if there's an error communicating with the product service
   */
  public List<ProductDto> getProductsByClientId(Long clientId) {
    log.debug("Fetching products for client ID: {}", clientId);

    String url = UriComponentsBuilder.fromUriString(productServiceUrl)
      .path("/api/v1/products/client/{clientId}")
      .buildAndExpand(clientId)
      .toUriString();

    try {
      ResponseEntity<List<ProductDto>> response = restTemplate.exchange(
        url,
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<>() {
        }
      );

      List<ProductDto> products = response.getBody();
      log.debug("Retrieved {} products for client ID: {}", products != null ? products.size() : 0, clientId);
      return products != null ? products : Collections.emptyList();
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("No products found for client ID: {}", clientId);
      return Collections.emptyList();
    } catch (ResourceAccessException e) {
      log.error("Network error while connecting to product service: {}", e.getMessage());
      throw new ProductServiceException("Unable to connect to product service: " + e.getMessage(), e);
    } catch (RestClientException e) {
      log.error("Error retrieving products for client ID {}: {}", clientId, e.getMessage());
      throw new ProductServiceException("Error retrieving products: " + e.getMessage(), e);
    }
  }

  /**
   * Возвращаем конкретный продкут по его идентификатору
   *
   * @param productId the ID of the product to retrieve
   * @return the product
   * @throws ProductNotFoundException if the product doesn't exist
   * @throws ProductServiceException  if there's an error communicating with the product service
   */
  public ProductDto getProductById(Long productId) {
    log.debug("Fetching product with ID: {}", productId);

    String url = UriComponentsBuilder.fromUriString(productServiceUrl)
      .path("/api/v1/products/{productId}")
      .buildAndExpand(productId)
      .toUriString();

    try {
      ProductDto product = restTemplate.getForObject(url, ProductDto.class);
      log.debug("Retrieved product: {}", product);
      return product;
    } catch (HttpClientErrorException.NotFound e) {
      log.warn("Product not found with ID: {}", productId);
      throw new ProductNotFoundException("Product not found with id: " + productId);
    } catch (ResourceAccessException e) {
      log.error("Network error while connecting to product service: {}", e.getMessage());
      throw new ProductServiceException("Unable to connect to product service: " + e.getMessage(), e);
    } catch (RestClientException e) {
      log.error("Error retrieving product with ID {}: {}", productId, e.getMessage());
      throw new ProductServiceException("Error retrieving product: " + e.getMessage(), e);
    }
  }
}