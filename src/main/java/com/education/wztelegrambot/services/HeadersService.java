package com.education.wztelegrambot.services;

import com.education.wztelegrambot.entities.HeaderData;
import com.education.wztelegrambot.entities.HeaderStatus;
import com.education.wztelegrambot.entities.UserEntity;
import com.education.wztelegrambot.repositories.HeadersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class HeadersService {
    @Autowired
    private HeadersRepository headersRepository;
    @Lazy
    @Autowired
    private TelegramService telegramService;

    private final static int MAX_EXCEPTION = 5;

    public HeaderData save(HeaderData headerData) {
        return headersRepository.save(headerData);
    }
    public void increaseHeaderLoadAttemptCount(HeaderData headerData) {
        headerData.increaseCount();
        if (headerData.getLoadAttemptCount() > MAX_EXCEPTION) {
            headerData.setHeaderStatus(HeaderStatus.ERROR);

            //Уведомление пользователя об ошибке
            telegramService.sendReturnedMessage(
                    headerData.getUser().getChatId(),
                    "Headers are out of date",
                    null,
                    null
            );
        }
        headersRepository.save(headerData);

    }

    public void removeAllByUserId(UserEntity user) {
        headersRepository.updateHeaderStatusByUser(HeaderStatus.REJECT, user);
    }
}
