package ru.learning.java.spring.service;

import org.springframework.stereotype.Service;
import ru.learning.java.spring.exception.InsufficientFundsException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class AccountService {
  // Простая in-memory реализация для демонстрации
  private final Map<Long, BigDecimal> accountBalances = new HashMap<>();

  public AccountService() {
    // Инициализация тестовыми данными
    accountBalances.put(1L, new BigDecimal("1000.00"));
    accountBalances.put(2L, new BigDecimal("500.00"));
    accountBalances.put(3L, new BigDecimal("100.00"));
  }

  public boolean hasEnoughBalance(Long userId, BigDecimal amount) {
    BigDecimal balance = accountBalances.getOrDefault(userId, BigDecimal.ZERO);
    return balance.compareTo(amount) >= 0;
  }

  public void debitAccount(Long userId, BigDecimal amount) {
    BigDecimal balance = accountBalances.getOrDefault(userId, BigDecimal.ZERO);

    if (balance.compareTo(amount) < 0) {
      throw new InsufficientFundsException("Недостаточно средств на счете");
    }

    accountBalances.put(userId, balance.subtract(amount));
  }

  public BigDecimal getBalance(Long userId) {
    return accountBalances.getOrDefault(userId, BigDecimal.ZERO);
  }
}
