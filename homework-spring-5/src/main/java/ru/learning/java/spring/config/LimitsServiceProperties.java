package ru.learning.java.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

/**
 * Класс конфигурации
 */
@Configuration
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