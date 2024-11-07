package com.education.wztelegrambot.telegram.handlers;

import com.education.wztelegrambot.entities.UserEntity;
import com.education.wztelegrambot.services.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class StartUpdateHandler implements UpdateHandler {

    private final TelegramService telegramService;
    public final static String START_HANDLER_COMMAND = "/start";
    @Override
    public void handle(Update update, UserEntity userEntity) {
        telegramService.sendStartMessage(userEntity.getChatId());
    }

    @Override
    public String getHandlerListName() {
        return START_HANDLER_COMMAND;
    }
}
