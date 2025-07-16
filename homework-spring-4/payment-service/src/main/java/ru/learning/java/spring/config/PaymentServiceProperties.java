package ru.learning.java.spring.config;

import org.springframework.beans.factory.annotation.Value;
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
  @Value("${payment-service.max-failed-payments}")
  private int maxFailedPayments;

  /**
   * Тайм-аут в миллисекундах для подключения к внешним сервисам
   */
  @Value("${payment-service.connection-timeout}")
  private int connectionTimeout;

  /**
   * Тайм-аут в миллисекундах для чтения из внешних служб
   */
  @Value("${payment-service.read-timeout}")
  private int readTimeout;

  /**
   * Включать ли аналитику платежей
   */
  @Value("${payment-service.analytics-enabled}")
  private boolean analyticsEnabled;

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