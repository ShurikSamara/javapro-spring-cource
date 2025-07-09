
package ru.learning.java.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.learning.java.spring.model.Product;
import ru.learning.java.spring.model.ProductType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  // Базовые поисковые методы
  List<Product> findByClientId(Long clientId);

  List<Product> findByProductType(ProductType productType);

  Optional<Product> findByAccountNumber(String accountNumber);

  List<Product> findByClientIdAndProductType(Long clientId, ProductType productType);

  List<Product> findByPriceGreaterThanEqual(BigDecimal minPrice);

  List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

  @Query("SELECT p FROM Product p WHERE p.balance >= :minBalance")
  List<Product> findByBalanceGreaterThanEqual(@Param("minBalance") BigDecimal minBalance);

  List<Product> findByNameContainingIgnoreCase(String name);

  boolean existsByAccountNumber(String accountNumber);

  boolean existsByClientIdAndProductType(Long clientId, ProductType productType);

  @Query("SELECT COUNT(p) FROM Product p WHERE p.clientId = :clientId")
  long countByClientId(@Param("clientId") Long clientId);

  @Query("SELECT p.productType, COUNT(p) FROM Product p GROUP BY p.productType")
  List<Object[]> getProductCountByType();

  @Query("SELECT SUM(p.price) FROM Product p WHERE p.clientId = :clientId")
  BigDecimal getTotalPriceByClientId(@Param("clientId") Long clientId);

  @Query("SELECT p FROM Product p WHERE p.balance > (SELECT AVG(p2.balance) FROM Product p2)")
  List<Product> findProductsAboveAveragePrice();

  List<Product> findByClientIdOrderByPriceDesc(Long clientId);

  List<Product> findByClientIdOrderByCreatedAtDesc(Long clientId);

  @Query("SELECT p FROM Product p WHERE p.accountNumber = :accountNumber AND p.id != :excludeId")
  Optional<Product> findByAccountNumberExcludingId(@Param("accountNumber") String accountNumber, @Param("excludeId") Long excludeId);

  @Query("SELECT p FROM Product p WHERE p.balance < :threshold AND p.productType = :type")
  List<Product> findLowBalanceProducts(@Param("threshold") BigDecimal threshold, @Param("type") ProductType type);
}