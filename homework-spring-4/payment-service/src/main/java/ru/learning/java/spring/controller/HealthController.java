package ru.learning.java.spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller для health check endpoints
 */
@RestController
@RequestMapping("/payments/health")
public class HealthController {
  private static final Logger log = LoggerFactory.getLogger(HealthController.class);
  private final LocalDateTime startTime = LocalDateTime.now();

  /**
   * Создадим health-check endpoint для удобства отслеживания живой ли сервис
   */
  @GetMapping
  public ResponseEntity<Map<String, Object>> healthCheck() {
    log.debug("Health check requested");

    Map<String, Object> healthInfo = new HashMap<>();
    healthInfo.put("status", "UP");
    healthInfo.put("timestamp", LocalDateTime.now());
    healthInfo.put("uptime", getUptime());

    return ResponseEntity.ok(healthInfo);
  }

  private String getUptime() {
    LocalDateTime now = LocalDateTime.now();
    long seconds = java.time.Duration.between(startTime, now).getSeconds();
    long days = seconds / (24 * 3600);
    seconds = seconds % (24 * 3600);
    long hours = seconds / 3600;
    seconds = seconds % 3600;
    long minutes = seconds / 60;
    seconds = seconds % 60;

    return String.format("%d days, %d hours, %d minutes, %d seconds", days, hours, minutes, seconds);
  }
}