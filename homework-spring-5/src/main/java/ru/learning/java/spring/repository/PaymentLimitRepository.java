package ru.learning.java.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.learning.java.spring.model.PaymentLimit;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Репозиторий управления лимитами платежей
 */
@Repository
public interface PaymentLimitRepository extends JpaRepository<PaymentLimit, Long> {

    /**
     * Ищем лимит по ID пользователя
     *
     * @param userId the user ID
     * @return the payment limit, if found
     */
    Optional<PaymentLimit> findByUserId(Long userId);

    /**
     * Обновляем текущий лимит пользователя
     *
     * @param userId       the user ID
     * @param currentLimit the new current limit
     * @param lastUpdated  the last updated timestamp
     * @return the number of rows affected
     */
    @Modifying
    @Query("UPDATE PaymentLimit pl SET pl.currentLimit = :currentLimit, pl.lastUpdated = :lastUpdated WHERE pl.userId = :userId")
    int updateCurrentLimit(@Param("userId") Long userId, @Param("currentLimit") BigDecimal currentLimit, @Param("lastUpdated") LocalDateTime lastUpdated);

    /**
     * Сбросить все лимиты до значений по умолчанию
     *
     * @param lastUpdated the last updated timestamp
     * @return the number of rows affected
     */
    @Modifying
    @Query("UPDATE PaymentLimit pl SET pl.currentLimit = pl.defaultLimit, pl.lastUpdated = :lastUpdated")
    int resetAllLimits(@Param("lastUpdated") LocalDateTime lastUpdated);
}