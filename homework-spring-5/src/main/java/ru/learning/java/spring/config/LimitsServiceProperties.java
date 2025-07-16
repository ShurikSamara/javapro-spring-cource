package ru.learning.java.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

/**
 * Класс конфигурации
 */

@ConfigurationProperties(prefix = "limits-service")
public class LimitsServiceProperties {

  @Value("${limits-service.default-limit}")
  private BigDecimal defaultLimit;

  @Value("${limits-service.reset-cron}")
  private String resetCron;

  public BigDecimal getDefaultLimit() {
    return defaultLimit;
  }

  public void setDefaultLimit(BigDecimal defaultLimit) {
    this.defaultLimit = defaultLimit;
  }

  public String getResetCron() {
    return resetCron;
  }

  public void setResetCron(String resetCron) {
    this.resetCron = resetCron;
  }
}