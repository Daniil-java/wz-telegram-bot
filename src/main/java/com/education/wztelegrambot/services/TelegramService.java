package com.education.wztelegrambot.services;

import com.education.wztelegrambot.telegram.TelegramBot;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

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

}
