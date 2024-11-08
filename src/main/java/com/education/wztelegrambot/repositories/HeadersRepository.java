package com.education.wztelegrambot.repositories;

import com.education.wztelegrambot.entities.HeaderData;
import com.education.wztelegrambot.entities.HeaderStatus;
import com.education.wztelegrambot.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface HeadersRepository extends JpaRepository<HeaderData, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE HeaderData h SET h.headerStatus = :status WHERE h.user = :user")
    void updateHeaderStatusByUser(@Param("user") UserEntity user, @Param("status") HeaderStatus status);

}
