package com.education.wztelegrambot.repositories;

import com.education.wztelegrambot.entities.HeaderData;
import com.education.wztelegrambot.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeadersRepository extends JpaRepository<HeaderData, Long> {
    void deleteAllByUser(UserEntity user);
}
