package com.education.wztelegrambot.telegram.handlers;

import com.education.wztelegrambot.entities.ProcessingStatus;
import com.education.wztelegrambot.entities.UserEntity;
import com.education.wztelegrambot.services.OrderService;
import com.education.wztelegrambot.services.TelegramService;
import com.education.wztelegrambot.telegram.TelegramBot;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

//Обработчик callback-запроса на генерацию сопроводительного письма
@Component
@AllArgsConstructor
public class CoverLetterCallbackUpdateHandler implements UpdateHandler{
    private static final String APPLIED_COMMAND = ProcessingStatus.APPLIED.name();
    private static final String REJECTED_COMMAND = ProcessingStatus.REJECTED.name();

    public static final String REQUEST_HANDLER_COMMAND = "/decision";
    private final OrderService orderService;
    private final TelegramService telegramService;

    @Override
    public void handle(Update update, UserEntity userEntity) {
        //Извлечение необходиммых - для ответа - данных
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer replyMessageId = callbackQuery.getMessage().getMessageId();
        String callbackData = callbackQuery.getData();

        //Получеие команды из callback data
        String decision = callbackData.split(TelegramBot.DELIMITER)[1];

        if (decision.startsWith(APPLIED_COMMAND)) {

            //Получение orderId callback data
            long orderId = Long.parseLong(decision.substring(APPLIED_COMMAND.length()));

            //Генерация сопроводительного письма
            String coverLetter = orderService.fetchGenerateCoverLetter(orderId, userEntity.getInfo());

            //Отправка сообщения пользователю и обработка, в случае удачного отправления
            if (coverLetter != null && telegramService.sendReturnedMessage
                    (chatId, coverLetter, null, replyMessageId) != null) {

                //Изменение статуса заказа
                orderService.orderSetApplyById(orderId);
            } else {

                //Сообщение об ошибке, в случае неудачи генерации письма
                telegramService.sendErrorMessage(chatId, replyMessageId);
            }

        } else if (decision.startsWith(REJECTED_COMMAND)) {

            //Получение orderId callback data
            long orderId = Long.parseLong(decision.substring(REJECTED_COMMAND.length()));

            //Изменение статуса заказа
            orderService.orderSetRejectById(orderId);
        }
    }

    @Override
    public String getHandlerListName() {
        return REQUEST_HANDLER_COMMAND;
    }
}
