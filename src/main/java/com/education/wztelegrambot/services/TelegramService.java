package com.education.wztelegrambot.services;

import com.education.wztelegrambot.entities.Order;
import com.education.wztelegrambot.entities.ProcessingStatus;
import com.education.wztelegrambot.telegram.TelegramBot;
import com.education.wztelegrambot.telegram.handlers.CoverLetterCallbackUpdateHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class TelegramService {

    private final TelegramBot telegramBot;

    public Message sendReturnedMessage(long chatId, String text,
                                       InlineKeyboardMarkup inlineKeyboardMarkup, Integer replyMessageId) {
        return telegramBot.sendReturnedMessage(
                SendMessage.builder()
                        .chatId(chatId)
                        .text(text)
                        .replyMarkup(inlineKeyboardMarkup)
                        .replyToMessageId(replyMessageId)
                        .parseMode(ParseMode.HTML)
                        .disableWebPagePreview(true)
                        .build()
        );
    }

    public Message sendReturnedMessage(long chatId, String text) {
        return telegramBot.sendReturnedMessage(
                SendMessage.builder()
                        .chatId(chatId)
                        .text(text)
                        .parseMode(ParseMode.HTML)
                        .disableWebPagePreview(true)
                        .build()
        );
    }

    public Message sendOrder(Order order) {
        StringBuilder builder = new StringBuilder();
        builder.append("<strong>");
        builder.append(order.getSubject()).append("\n");
        builder.append("</strong>");
        builder.append(order.getDescription());

        return sendReturnedMessage(order.getUser().getTelegramId(), builder.toString(),
                getOrderButtons(order.getId()), null);
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
}
