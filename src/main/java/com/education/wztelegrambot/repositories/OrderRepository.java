package com.education.wztelegrambot.repositories;

import com.education.wztelegrambot.entities.Order;
import com.education.wztelegrambot.entities.ProcessingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByWzId(Long wzId);

    List<Order> findAllByProcessingStatus(ProcessingStatus processingStatus);
}
