package ru.learning.java.spring.model;

public class Product {
  private Long id;
  private String accountNumber;
  private Double balance;
  private ProductType productType;
  private Long userId;

  public Product() {}

  public Product(String accountNumber, Double balance, ProductType productType, Long userId) {
    this.accountNumber = accountNumber;
    this.balance = balance;
    this.productType = productType;
    this.userId = userId;
  }

  public Product(Long id, String accountNumber, Double balance, ProductType productType, Long userId) {
    this.id = id;
    this.accountNumber = accountNumber;
    this.balance = balance;
    this.productType = productType;
    this.userId = userId;
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

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public Double getBalance() {
    return balance;
  }

  public void setBalance(Double balance) {
    this.balance = balance;
  }

  public ProductType getProductType() {
    return productType;
  }

  public void setProductType(ProductType productType) {
    this.productType = productType;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  @Override
  public String toString() {
    return "Product{" +
      "id=" + id +
      ", accountNumber='" + accountNumber + '\'' +
      ", balance=" + balance +
      ", productType=" + productType +
      ", userId=" + userId +
      '}';
  }
}