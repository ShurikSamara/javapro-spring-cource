package ru.learning.java.spring.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.learning.java.spring.dto.ProductDto;
import ru.learning.java.spring.exception.ProductNotFoundException;
import ru.learning.java.spring.exception.ProductServiceException;

import java.util.List;

@Service
public class ProductServiceClient {
  private final RestTemplate restTemplate;
  private final String productServiceUrl;

  public ProductServiceClient(RestTemplate restTemplate, String productServiceUrl) {
    this.restTemplate = restTemplate;
    this.productServiceUrl = productServiceUrl;
  }

  public ProductDto getProductById(Long productId) {
    String url = productServiceUrl + productId;
    return executeRequest(url, ProductDto.class);
  }

  public List<ProductDto> getProductsByClientId(Long clientId) {
    String url = productServiceUrl + clientId;
    return executeRequest(url, new ParameterizedTypeReference<List<ProductDto>>() {});
  }

  /**
   * Универсальный метод для выполнения GET запросов
   */
  private <T> T executeRequest(String url, Object responseType) {
    try {
      ResponseEntity<T> response;

      if (responseType instanceof Class) {
        response = restTemplate.exchange(url, HttpMethod.GET, null, (Class<T>) responseType);
      } else if (responseType instanceof ParameterizedTypeReference) {
        response = restTemplate.exchange(url, HttpMethod.GET, null, (ParameterizedTypeReference<T>) responseType);
      } else {
        throw new IllegalArgumentException("Unsupported response type: " + responseType.getClass());
      }

      return response.getBody();
    } catch (HttpClientErrorException.NotFound e) {
      throw new ProductNotFoundException("Resource not found: " + url);
    } catch (ResourceAccessException e) {
      throw new ProductServiceException("Unable to connect to product service", e);
    } catch (RestClientException e) {
      throw new ProductServiceException("Error calling product service", e);
    }
  }
}