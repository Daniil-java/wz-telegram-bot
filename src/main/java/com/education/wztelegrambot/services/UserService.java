package com.education.wztelegrambot.services;

import com.education.wztelegrambot.entities.UserEntity;
import com.education.wztelegrambot.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserEntity getOrCreateUser(User userInfo) {
        long telegramId = userInfo.getId();
        Optional<UserEntity> userEntity = userRepository.findUserEntityByTelegramId(telegramId);
        if (userEntity.isPresent()) {
            return userEntity.get();
        } else {
            return userRepository.save(
                    new UserEntity()
                            .setTelegramId(telegramId)
                            .setChatId(userInfo.getId())
                            .setUsername(userInfo.getUserName())
                            .setFirstname(userInfo.getFirstName())
                            .setLastname(userInfo.getLastName())
                            .setLanguageCode(userInfo.getLanguageCode())
            );
        }
    }

    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }

    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }


}
