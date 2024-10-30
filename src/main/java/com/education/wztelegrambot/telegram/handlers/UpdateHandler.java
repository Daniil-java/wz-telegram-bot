package com.education.wztelegrambot.telegram.handlers;

import com.education.wztelegrambot.entities.UserEntity;
import com.education.wztelegrambot.telegram.facades.TelegramFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateHandler {
    void handle(Update update, UserEntity userEntity);

    String getHandlerListName();

    @Autowired
    default void registerMyself(TelegramFacade messageFacade) {
        messageFacade.register(getHandlerListName(), this);
    }

}
