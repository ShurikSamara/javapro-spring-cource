package ru.learning.java.spring.service;

import org.springframework.stereotype.Service;
import ru.learning.java.spring.exception.InsufficientFundsException;
import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccountService {
  private final ConcurrentHashMap<Long, BigDecimal> accountBalances = new ConcurrentHashMap<>();

  public AccountService() {
    accountBalances.put(1L, new BigDecimal("1000.00"));
    accountBalances.put(2L, new BigDecimal("500.00"));
    accountBalances.put(3L, new BigDecimal("100.00"));
  }

  public boolean hasEnoughBalance(Long clientId, BigDecimal amount) {
    BigDecimal balance = accountBalances.getOrDefault(clientId, BigDecimal.ZERO);
    return balance.compareTo(amount) >= 0;
  }

  public synchronized void debitAccount(Long clientId, BigDecimal amount) {
    BigDecimal balance = accountBalances.getOrDefault(clientId, BigDecimal.ZERO);

    if (balance.compareTo(amount) < 0) {
      throw new InsufficientFundsException("Недостаточно средств на счете");
    }

    accountBalances.put(clientId, balance.subtract(amount));
  }

  public BigDecimal getBalance(Long clientId) {
    return accountBalances.getOrDefault(clientId, BigDecimal.ZERO);
  }
}