package ru.learning.java.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.learning.java.spring.config.PaymentServiceProperties;

@SpringBootApplication
@EnableConfigurationProperties(PaymentServiceProperties.class)
public class PaymentServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(PaymentServiceApplication.class, args);
  }
}