package com.education.wztelegrambot.telegram.handlers;

import com.education.wztelegrambot.entities.UserEntity;
import com.education.wztelegrambot.services.TelegramService;
import com.education.wztelegrambot.services.UserService;
import com.education.wztelegrambot.telegram.TelegramBot;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@AllArgsConstructor
//Обработчик для управления фильтром заказов
public class FilterUpdateHandler implements UpdateHandler {
    private final UserService userService;
    private final TelegramService telegramService;
    public static final String FILTER_HANDLER_COMMAND = "/filter";
    public static final String FILTER_CLEAR_COMMAND = "clear";
    @Override
    public void handle(Update update, UserEntity userEntity) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        String messageText = message.getText();
        String answer;

        String[] splitted = messageText.split(TelegramBot.DELIMITER);

        if (splitted.length == 1) {
            answer = "Вам нужно указать фильтр!";
        } else if (splitted[1].equals(FILTER_CLEAR_COMMAND)) {
            userService.save(userEntity.setFilter(null));
            answer = "Фильтр очищен!";
        } else {
            answer = userService.setFilter(userEntity, splitted[1]);
        }

        telegramService.sendReturnedMessage(chatId, answer);

    }

    @Override
    public String getHandlerListName() {
        return FILTER_HANDLER_COMMAND;
    }
}
