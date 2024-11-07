package com.education.wztelegrambot.telegram.handlers;

import com.education.wztelegrambot.entities.HeaderData;
import com.education.wztelegrambot.entities.HeaderStatus;
import com.education.wztelegrambot.entities.UserEntity;
import com.education.wztelegrambot.services.HeadersService;
import com.education.wztelegrambot.services.TelegramService;
import com.education.wztelegrambot.services.WzService;
import com.education.wztelegrambot.telegram.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class UserVariablesUpdateHandler implements UpdateHandler {
    private final static String RESPONSE = "Переменная сохранена";
    private final TelegramService telegramService;
    private final HeadersService headersService;
    private final WzService wzService;
    public final static String USER_VARIABLES_HANDLER_COMMAND = "/var";

    @Override
    public void handle(Update update, UserEntity userEntity) {
        //Извлеченные необходимых данных из запроса
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        String messageText = message.getText();
        String[] splitted = messageText.split(TelegramBot.DELIMITER);

        String answer = RESPONSE;
        //Проверка корректности введённой пользователем команды
        if (splitted.length < 2) {
            answer = "Проверьте название переменной";

            //Удаление всех заголоков пользователя по команде
        } else if (splitted[1].equals("clear")) {
            headersService.removeAllByUserId(userEntity);
            answer = "Данные удалены";
        } else {

            //Создание объекта заголовков
            HeaderData headerData = new HeaderData()
                    .setUser(userEntity)
                    .setCookies(splitted[1])
                    .setAgentId(splitted[2]);

            //Провека работы заголовков
            if (wzService.checkHeaders(headerData)) {

                //Сохранение при корректной работе
                headersService.save(headerData.setHeaderStatus(HeaderStatus.OK));
            } else {

                //Уведомление пользователя об ошибке
                answer = "Данные некорректны или сервис не отвечает";
            }

        }

        //Отправка ответа пользователю
        telegramService.sendReturnedMessage(chatId, answer);
    }

    @Override
    public String getHandlerListName() {
        return USER_VARIABLES_HANDLER_COMMAND;
    }
}
