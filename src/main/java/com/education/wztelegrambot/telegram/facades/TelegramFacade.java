package com.education.wztelegrambot.telegram.facades;

import com.education.wztelegrambot.entities.UserEntity;
import com.education.wztelegrambot.services.UserService;
import com.education.wztelegrambot.telegram.TelegramBot;
import com.education.wztelegrambot.telegram.handlers.ErrorUpdateHandler;
import com.education.wztelegrambot.telegram.handlers.UpdateHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramFacade {
    private final UserService userService;
    private Map<String, UpdateHandler> map = new ConcurrentHashMap<>();

    public void register(String botState, UpdateHandler updateHandler) {
        map.put(botState, updateHandler);
    }

    public void handleUpdate(Update update) {
        /*
         На этапе разработки обработка сообщений
         происходит только от одного пользователя
         */
        User user = update.getMessage() != null ?
                update.getMessage().getFrom() :
                update.getCallbackQuery().getFrom();
        if (user.getId() != 425120436L) return;

        UserEntity userEntity = userService.getOrCreateUser(user);
        processInputUpdate(update).handle(update, userEntity);
    }

    public UpdateHandler processInputUpdate(Update update) {
        String request;
        if (update.hasCallbackQuery()) {
            request = update.getCallbackQuery().getData();
        } else {
            request = update.getMessage().getText();
        }
        UpdateHandler currentUpdateHandler = map.get(request.split(TelegramBot.DELIMITER)[0]);
        return currentUpdateHandler == null ? map.get(ErrorUpdateHandler.class.getSimpleName()) : currentUpdateHandler;

    }
}
