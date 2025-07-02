package ru.learning.java.spring.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.learning.java.spring.model.Product;
import ru.learning.java.spring.model.ProductType;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductDao {

  private final DataSource dataSource;

  @Autowired
  public ProductDao(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public Product save(Product product) {
    String sql = "INSERT INTO products (account_number, balance, product_type, user_id) VALUES (?, ?, ?::product_type, ?) RETURNING id";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setString(1, product.getAccountNumber());
      statement.setDouble(2, product.getBalance());
      statement.setString(3, product.getProductType().name());
      statement.setLong(4, product.getUserId());

      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        product.setId(resultSet.getLong("id"));
      }
      return product;
    } catch (SQLException e) {
      throw new RuntimeException("Ошибка при сохранении продукта", e);
    }
  }

  public Optional<Product> findById(Long id) {
    String sql = "SELECT id, account_number, balance, product_type, user_id FROM products WHERE id = ?";
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setLong(1, id);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          Product product = new Product(
            resultSet.getLong("id"),
            resultSet.getString("account_number"),
            resultSet.getDouble("balance"),
            ProductType.valueOf(resultSet.getString("product_type")),
            resultSet.getLong("user_id")
          );
          return Optional.of(product);
        }
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException("Ошибка при поиске продукта", e);
    }
  }

  public List<Product> findByUserId(Long userId) {
    String sql = "SELECT id, account_number, balance, product_type, user_id FROM products WHERE user_id = ? ORDER BY id";
    List<Product> products = new ArrayList<>();

    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setLong(1, userId);

      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          Product product = new Product(
            resultSet.getLong("id"),
            resultSet.getString("account_number"),
            resultSet.getDouble("balance"),
            ProductType.valueOf(resultSet.getString("product_type")),
            resultSet.getLong("user_id")
          );
          products.add(product);
        }
      }
      return products;
    } catch (SQLException e) {
      throw new RuntimeException("Ошибка при получении продуктов пользователя", e);
    }
  }

  public List<Product> findAll() {
    String sql = "SELECT id, account_number, balance, product_type, user_id FROM products ORDER BY id";
    List<Product> products = new ArrayList<>();

    try (Connection connection = dataSource.getConnection();
         Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(sql)) {

      while (resultSet.next()) {
        Product product = new Product(
          resultSet.getLong("id"),
          resultSet.getString("account_number"),
          resultSet.getDouble("balance"),
          ProductType.valueOf(resultSet.getString("product_type")),
          resultSet.getLong("user_id")
        );
        products.add(product);
      }
      return products;
    } catch (SQLException e) {
      throw new RuntimeException("Ошибка при получении всех продуктов", e);
    }
  }
}