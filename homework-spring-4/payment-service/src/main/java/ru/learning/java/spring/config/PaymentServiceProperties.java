package ru.learning.java.spring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Конфиг для платёжного сервиса
 */
@Configuration
@ConfigurationProperties(prefix = "payment-service")
public class PaymentServiceProperties {

  /**
   * Максимальное количество неудавшихся платежей, разрешенное за 24 часа
   */
  private int maxFailedPayments = 5;

  /**
   * Тайм-аут в миллисекундах для подключения к внешним сервисам
   */
  private int connectionTimeout = 5000;

  /**
   * Тайм-аут в миллисекундах для чтения из внешних служб
   */
  private int readTimeout = 5000;

  /**
   * Включать ли аналитику платежей
   */
  private boolean analyticsEnabled = true;

  public int getMaxFailedPayments() {
    return maxFailedPayments;
  }

  public void setMaxFailedPayments(int maxFailedPayments) {
    this.maxFailedPayments = maxFailedPayments;
  }

  public int getConnectionTimeout() {
    return connectionTimeout;
  }

  public void setConnectionTimeout(int connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  public int getReadTimeout() {
    return readTimeout;
  }

  public void setReadTimeout(int readTimeout) {
    this.readTimeout = readTimeout;
  }

  public boolean isAnalyticsEnabled() {
    return analyticsEnabled;
  }

  public void setAnalyticsEnabled(boolean analyticsEnabled) {
    this.analyticsEnabled = analyticsEnabled;
  }
}