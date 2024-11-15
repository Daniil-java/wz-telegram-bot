package com.education.wztelegrambot.telegram.handlers;

import com.education.wztelegrambot.entities.UserEntity;
import com.education.wztelegrambot.services.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

//Обработчик непродусмотренных сообщений
@Component
@RequiredArgsConstructor
public class ErrorUpdateHandler implements UpdateHandler {
    private final static String RESPONSE = "Данный запрос непредусмотрен сервисом.";
    public final static String ERROR_HANDLER_COMMAND = "/about";
    private final TelegramService telegramService;
    @Override
    public void handle(Update update, UserEntity userEntity) {
        telegramService.sendReturnedMessage(
                userEntity.getChatId(),
                RESPONSE,
                null,
                null
        );
    }

    public void reportError(UserEntity userEntity, String errorText) {
        telegramService.sendReturnedMessage(
                userEntity.getChatId(),
                errorText,
                null,
                null
        );
    }

    @Override
    public String getHandlerListName() {
        return ERROR_HANDLER_COMMAND;
    }
}
