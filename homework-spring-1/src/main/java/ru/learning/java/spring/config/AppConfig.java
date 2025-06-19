package ru.learning.java.spring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.learning.java.spring.Application;

@Configuration
@ComponentScan(basePackageClasses = {Application.class})
public class AppConfig {
}
