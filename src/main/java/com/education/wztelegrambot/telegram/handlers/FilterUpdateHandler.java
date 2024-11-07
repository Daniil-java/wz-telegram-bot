package com.education.wztelegrambot.telegram.handlers;

import com.education.wztelegrambot.entities.ProcessingStatus;
import com.education.wztelegrambot.entities.UserEntity;
import com.education.wztelegrambot.services.OpenAiService;
import com.education.wztelegrambot.services.OrderService;
import com.education.wztelegrambot.services.TelegramService;
import com.education.wztelegrambot.services.UserService;
import com.education.wztelegrambot.telegram.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
//Обработчик для управления фильтром заказов
public class FilterUpdateHandler implements UpdateHandler {
    private final UserService userService;
    private final TelegramService telegramService;
    private final OpenAiService openAiService;
    private final OrderService orderService;
    public static final String FILTER_HANDLER_COMMAND = "/filter";
    public static final String FILTER_CLEAR_COMMAND = "clear";
    public static final String FILTER_APPLY_COMMAND = "apply";
    public static final String FILTER_REJECT_COMMAND = "reject";
    public static final String FILTER_NEW_COMMAND = "new";
    @Override
    public void handle(Update update, UserEntity userEntity) {
        if (update.hasCallbackQuery()) {
            callbackHandle(update, userEntity);
        } else {
            messageHandle(update, userEntity);
        }
    }

    private void callbackHandle(Update update, UserEntity userEntity) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        Long chatId = callbackQuery.getMessage().getChatId();
        String[] splitted = callbackQuery.getData().split(TelegramBot.DELIMITER);

        String decision = splitted[1];

        //При положительном ответе, произойдет отправка, в зависимости от предыдущего значения фильтра
        if (decision.equals(FILTER_APPLY_COMMAND)) {
            ProcessingStatus processingStatus;
            //Если новой фильтр является пустым
            if (splitted.length < 3) {

                //При статусе ANALYZED, заказы не будут повторно проверены AI
                //Поле isMatchingFilter изменит значение на true
                orderService.updateNotMatchingOrdersForNotificationByUser(userEntity);

            } else {

                //Заказы будут проверены повторно, с учетом нового фильтра
                orderService.updateNotMatchingOrdersProcessingStatusForReAnalyzeByUser(userEntity);
            }
        }

        telegramService.deleteMessage(messageId, chatId);

    }

    private void messageHandle(Update update, UserEntity userEntity) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        String messageText = message.getText();
        String answer;

        String[] splitted = messageText.split(TelegramBot.DELIMITER);

        //Базовая проверка корректности запроса
        if (splitted.length == 1) {
            answer = "Вам нужно указать фильтр!";

            //Запрос на очистку фильтра
        } else if (splitted[1].equals(FILTER_CLEAR_COMMAND)) {
            if (userEntity.getFilter() == null) {
                answer = "Фильтр уже пуст!";
            } else {
                userService.clearFilter(userEntity);
                answer = "Фильтр очищен!";
                telegramService.sendMessageAfterFilterReset(userEntity, null);
            }

            //Установка нового фильтра
        } else {
            //Если фильтр не был пустым, запросит проверку пропущенных, с учетом нового фильтра
            if (userEntity.getFilter() != null) {
                telegramService.sendMessageAfterFilterReset(userEntity, FILTER_NEW_COMMAND);
            }

            //Если фильтр ранее был null, нет необходимости проверять, ранее непрошидшие фильтр, заказы
            userEntity = userService.setFilter(userEntity, splitted[1]);

            //Запрос к OpenAI на понимание нового фильтра
            answer = openAiService.analyzeFilter(userEntity.getFilter());

        }

        telegramService.sendReturnedMessage(chatId, answer);

    }

    @Override
    public String getHandlerListName() {
        return FILTER_HANDLER_COMMAND;
    }
}
