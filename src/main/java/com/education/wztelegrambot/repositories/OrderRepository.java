package com.education.wztelegrambot.repositories;

import com.education.wztelegrambot.entities.Order;
import com.education.wztelegrambot.entities.ProcessingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByWzId(Long wzId);

    List<Order> findAllByProcessingStatus(ProcessingStatus processingStatus);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.processingStatus = :status WHERE o.id = :id")
    void updateStatusById(@Param("id") Long orderId, @Param("status") ProcessingStatus status);
}
