package com.education.wztelegrambot.repositories;

import com.education.wztelegrambot.entities.Order;
import com.education.wztelegrambot.entities.ProcessingStatus;
import com.education.wztelegrambot.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByWzId(Long wzId);

    Optional<Order> findByWzIdAndUser(Long wzId, UserEntity user);

    List<Order> findAllByProcessingStatus(ProcessingStatus processingStatus);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.processingStatus = :status WHERE o.id = :id")
    void updateStatusById(@Param("id") Long orderId, @Param("status") ProcessingStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.processingStatus = :newStatus " +
            "WHERE o.user = :user " +
            "AND o.processingStatus = :currentStatus " +
            "AND o.created >= :timePeriod")
    void updateOrderStatusByUserAndStatus(
            @Param("user") UserEntity user,
            @Param("currentStatus") ProcessingStatus currentStatus,
            @Param("newStatus") ProcessingStatus newStatus,
            @Param("timePeriod") LocalDateTime timePeriod);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.processingStatus = :newStatus, " +
            "o.isMatchingFilter = true " +
            "WHERE o.user = :user " +
            "AND o.processingStatus = 'NOT_MATCHING_FILTER' " +
            "AND o.created >= :timePeriod")
    void updateNotMatchingOrdersForNotificationByUser(
            @Param("user") UserEntity user,
            @Param("newStatus") ProcessingStatus newStatus,
            @Param("timePeriod") LocalDateTime timePeriod);
}
