package com.education.wztelegrambot.services;

import com.education.wztelegrambot.entities.Order;
import com.education.wztelegrambot.entities.ProcessingStatus;
import com.education.wztelegrambot.telegram.TelegramBot;
import com.education.wztelegrambot.telegram.handlers.CoverLetterCallbackUpdateHandler;
import com.education.wztelegrambot.telegram.handlers.UserVariablesUpdateHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
@AllArgsConstructor
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
        telegramBot.sendReturnedMessage(
                SendMessage.builder()
                        .chatId(chatId)
                        .text("Произошла ошибка")
                        .parseMode(ParseMode.HTML)
                        .disableWebPagePreview(true)
                        .build()
        );
    }

    public void sendErrorMessage(Long chatId, Integer replyMessageId) {
         telegramBot.sendReturnedMessage(
                SendMessage.builder()
                        .chatId(chatId)
                        .text("Произошла ошибка")
                        .replyToMessageId(replyMessageId)
                        .parseMode(ParseMode.HTML)
                        .disableWebPagePreview(true)
                        .build()
        );
    }

    public void sendStartMessage(Long chatId) {
        String startMessage = "Этот бот предназначен для упрощения работы с различными фриланс сервисами";
        sendReturnedMessage(chatId, startMessage, getStartReplyKeyboard(), null);

    }

    private ReplyKeyboardMarkup getStartReplyKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        KeyboardRow keyboardButtons = new KeyboardRow();
        keyboardButtons.add(UserVariablesUpdateHandler.USER_VARIABLES_HANDLER_COMMAND + TelegramBot.DELIMITER + "clear");

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
}
