package ru.learning.java.spring.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

  private final DatabaseProperties databaseProperties;

  public DatabaseConfig(DatabaseProperties databaseProperties) {
    this.databaseProperties = databaseProperties;
  }

  @Bean
  public DataSource dataSource() {
    HikariConfig config = new HikariConfig();

    config.setJdbcUrl(databaseProperties.getJdbcUrl());
    config.setUsername(databaseProperties.getUser());
    config.setPassword(databaseProperties.getPassword());
    config.setDriverClassName(databaseProperties.getDriver());

    // Настройки пула соединений из properties
    config.setMaximumPoolSize(databaseProperties.getMaximumPoolSize());
    config.setMinimumIdle(databaseProperties.getMinimumIdle());
    config.setConnectionTimeout(databaseProperties.getConnectionTimeout());
    config.setIdleTimeout(databaseProperties.getIdleTimeout());
    config.setMaxLifetime(databaseProperties.getMaxLifetime());
    config.setLeakDetectionThreshold(databaseProperties.getLeakDetectionThreshold());
    config.setConnectionTestQuery(databaseProperties.getConnectionTestQuery());

    return new HikariDataSource(config);
  }
}