package com.education.wztelegrambot.telegram.handlers;

import com.education.wztelegrambot.entities.HeaderData;
import com.education.wztelegrambot.entities.HeaderStatus;
import com.education.wztelegrambot.entities.UserEntity;
import com.education.wztelegrambot.services.HeadersService;
import com.education.wztelegrambot.services.TelegramService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@AllArgsConstructor
public class UserVariablesUpdateHandler implements UpdateHandler {
    private final static String RESPONSE = "Переменная сохранена";
    private final TelegramService telegramService;
    private final HeadersService headersService;

    @Override
    public void handle(Update update, UserEntity userEntity) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        String messageText = message.getText();
        String[] splitted = messageText.split("!#!");

        String answer = RESPONSE;
        if (splitted.length < 2) {
            answer = "Проверьте название переменной";
        } else if (splitted[1].equals("clear")) {
            headersService.removeAllByUserId(userEntity);
            answer = "Данные удалены";
        } else {
            headersService.save(new HeaderData()
                    .setUser(userEntity)
                    .setCookies(splitted[1])
                    .setAgentId(splitted[2])
                    .setHeaderStatus(HeaderStatus.OK));
        }

        telegramService.sendReturnedMessage(
                chatId,
                answer,
                null, null
        );

    }

    @Override
    public String getHandlerListName() {
        return "/var";
    }
}
