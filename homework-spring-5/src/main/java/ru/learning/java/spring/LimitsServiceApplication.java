package ru.learning.java.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.learning.java.spring.config.LimitsServiceProperties;


@SpringBootApplication
@EnableConfigurationProperties(LimitsServiceProperties.class)
@EnableScheduling
public class LimitsServiceApplication {
    private static final Logger log = LoggerFactory.getLogger(LimitsServiceApplication.class);

    public static void main(String[] args) {
        log.info("Starting Limits Service Application");
        SpringApplication.run(LimitsServiceApplication.class, args);
    }
}