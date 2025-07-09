package ru.learning.java.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.learning.java.spring.model.Product;
import ru.learning.java.spring.model.ProductType;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  Product getProductById(Long productId);

  List<Product> findByClientId(Long clientId);

  List<Product> findByProductType(ProductType productType);

  @Query("SELECT p FROM Product p WHERE p.clientId = :clientId AND p.productType = :productType")
  List<Product> findByClientIdAndProductType(@Param("clientId") Long clientId, @Param("productType") ProductType productType);

  @Query("SELECT p FROM Product p WHERE p.price >= :minBalance")
  List<Product> findByBalanceGreaterThanEqual(@Param("minBalance") BigDecimal minBalance);

  boolean existsByAccountNumber(String accountNumber);
}