package com.education.wztelegrambot.repositories;

import com.education.wztelegrambot.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findUserEntityByTelegramId(long telegramId);
}
