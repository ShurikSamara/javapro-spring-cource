package ru.learning.java.spring.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "account_number", nullable = false)
  private String accountNumber;

  @Column(nullable = false)
  private BigDecimal price;

  @Enumerated(EnumType.STRING)
  @Column(name = "product_type", nullable = false)
  private ProductType productType;

  @Column(name = "client_id", nullable = false)
  private Long clientId;

  public Product() {}

  public Product(String accountNumber, BigDecimal price, ProductType productType, Long clientId) {
    this.accountNumber = accountNumber;
    this.price = price;
    this.productType = productType;
    this.clientId = clientId;
  }

  public Product(Long id, String accountNumber, BigDecimal price, ProductType productType, Long clientId) {
    this.id = id;
    this.accountNumber = accountNumber;
    this.price = price;
    this.productType = productType;
    this.clientId = clientId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal balance) {
    this.price = balance;
  }

  public ProductType getProductType() {
    return productType;
  }

  public Long getClientId() {
    return clientId;
  }

  @Override
  public String toString() {
    return "Product{" +
      "id=" + id +
      ", accountNumber='" + accountNumber + '\'' +
      ", price =" + price +
      ", productType=" + productType +
      ", clientId=" + clientId +
      '}';
  }
}