package com.education.wztelegrambot.services;

import com.education.wztelegrambot.entities.Order;
import com.education.wztelegrambot.entities.ProcessingStatus;
import com.education.wztelegrambot.entities.UserEntity;
import com.education.wztelegrambot.telegram.TelegramBot;
import com.education.wztelegrambot.telegram.handlers.CoverLetterCallbackUpdateHandler;
import com.education.wztelegrambot.telegram.handlers.FilterUpdateHandler;
import com.education.wztelegrambot.telegram.handlers.UserVariablesUpdateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramService {

    private final TelegramBot telegramBot;

    public Message sendReturnedMessage(long chatId, String text,
                                       ReplyKeyboard replyKeyboard, Integer replyMessageId) {

        return telegramBot.sendReturnedMessage(buildMessage(chatId, text, replyKeyboard, replyMessageId));
    }

    public Message sendReturnedMessage(long chatId, String text) {
        return sendReturnedMessage(chatId, text, null, null);
    }

    public Message sendOrder(Order order) {
        StringBuilder builder = new StringBuilder();
        builder.append("<strong>")
                .append(order.getSubject())
                .append("[")
                .append(order.isDevelopment() ? "\uD83D\uDCBB" : "❌")
                .append("|")
                .append(order.isSolvableByAi() ? "\uD83E\uDD16" : "❌")
                .append("]")
                .append("</strong>")
                .append("\n");

        builder.append(order.getUrl()).append("\n");

        builder.append(convertMillisToDaysHours(order.getDuration()))
                .append("\n")
                .append(order.getPrice())
                .append(" руб.")
                .append("\n")
                .append("\n")
                .append(order.getDescription());

        return sendReturnedMessage(order.getUser().getTelegramId(), builder.toString(),
                getOrderButtons(order.getId()), null);
    }

    public Message sendMessageAfterFilterReset(UserEntity user, String command) {
        return sendReturnedMessage(user.getChatId(), "Вывести пропущенные заказы за 24 часа?",
                getButtonsAfterResetFilter(command), null);
    }

    private InlineKeyboardMarkup getButtonsAfterResetFilter(String command) {
        command = command == null ? "" : TelegramBot.DELIMITER + command;
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton applyButton = new InlineKeyboardButton("Принять");
        InlineKeyboardButton rejectButton = new InlineKeyboardButton("Отклонить");

        applyButton.setCallbackData(FilterUpdateHandler.FILTER_HANDLER_COMMAND + TelegramBot.DELIMITER +
                FilterUpdateHandler.FILTER_APPLY_COMMAND + command) ;
        rejectButton.setCallbackData(FilterUpdateHandler.FILTER_HANDLER_COMMAND + TelegramBot.DELIMITER +
                FilterUpdateHandler.FILTER_REJECT_COMMAND + command);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(Arrays.asList(applyButton, rejectButton));

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    private static String convertMillisToDaysHours(long millis) {
        long hours = millis / (1000 * 60 * 60);
        long days = hours / 24;
        hours %= 24;

        return String.format("%d дней, %d часов", days, hours);
    }

    private InlineKeyboardMarkup getOrderButtons(long orderId) {
        String callbackCommand = CoverLetterCallbackUpdateHandler.REQUEST_HANDLER_COMMAND;
        String delimiter = TelegramBot.DELIMITER;
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton generateButton =
                new InlineKeyboardButton("Принять");
        InlineKeyboardButton rejectButton =
                new InlineKeyboardButton("Отклонить");

        generateButton.setCallbackData(callbackCommand + delimiter + ProcessingStatus.APPLIED.name() + orderId);
        rejectButton.setCallbackData(callbackCommand + delimiter + ProcessingStatus.REJECTED.name() + orderId);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(Arrays.asList(generateButton, rejectButton));

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public void sendErrorMessage(Long chatId) {
        sendErrorMessage(chatId);
    }

    public void sendErrorMessage(Long chatId, Integer replyMessageId) {
        String error = "Произошла ошибка";
         telegramBot.sendReturnedMessage(
                 buildMessage(chatId, error, null, replyMessageId));
    }

    public void sendStartMessage(Long chatId) {
        String startMessage = "Этот бот предназначен для упрощения работы с различными фриланс сервисами";
        sendReturnedMessage(chatId, startMessage, getStartReplyKeyboard(), null);

    }

    private ReplyKeyboardMarkup getStartReplyKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        KeyboardRow keyboardButtons = new KeyboardRow();
        keyboardButtons.add(UserVariablesUpdateHandler.USER_VARIABLES_HANDLER_COMMAND + TelegramBot.DELIMITER + "clear");
        keyboardButtons.add(FilterUpdateHandler.FILTER_HANDLER_COMMAND + TelegramBot.DELIMITER + FilterUpdateHandler.FILTER_CLEAR_COMMAND);

        keyboardMarkup.setKeyboard(Collections.singletonList(keyboardButtons));

        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    private SendMessage buildMessage(long chatId, String text,
                                     ReplyKeyboard replyKeyboard, Integer replyMessageId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(replyKeyboard)
                .replyToMessageId(replyMessageId)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .build();
    }

    public void deleteMessage(Integer messageId, Long chatId) {
        telegramBot.sendMessage(DeleteMessage.builder()
                .messageId(messageId)
                .chatId(chatId)
                .build());
    }
}
