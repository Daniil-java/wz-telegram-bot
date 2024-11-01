package com.education.wztelegrambot.services;

import com.education.wztelegrambot.configurations.OpenAiKeyConfiguration;
import com.education.wztelegrambot.dtos.OpenAiAnalyzeResponseDto;
import com.education.wztelegrambot.entities.Order;
import com.education.wztelegrambot.entities.openai.OpenAiChatCompletionRequest;
import com.education.wztelegrambot.entities.openai.OpenAiChatCompletionResponse;
import com.education.wztelegrambot.integrations.OpenAiFeignClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAiService {
    private final OpenAiFeignClient openAiFeignClient;
    private final OpenAiKeyConfiguration openAiKeyConfiguration;

    public OpenAiAnalyzeResponseDto analyzeOrder(Order order) throws JsonProcessingException {
        //Создание текста запроса для OpenAI
        String request = String.format(
                "Я отправляю тебе описание заказа. " +
                        "Тебе нужно ответить в JSON-формате. И ничего кроме него.\n" +
                        "Используй только текстовое сообщение, без чего-то подобного ```json" +
                        "Тебе нужно ответить на 2 вопроса, по данному заказу. " +
                        "1: Относится ли данный заказ к IT, разработке. (JSON: 'isDevelopment : boolean')" +
                        "2. Сможешь ли ты, самостоятельно выполнить этот заказ. (JSON: 'isSolvableByAi : boolean')\n" +
                        "Название заказа: %s\n" +
                        "Описание: %s", order.getSubject(), order.getDescription());

        //Отправка запроса и получение ответа OpenAI
        OpenAiChatCompletionResponse response = openAiFeignClient.generate(
                "Bearer " + openAiKeyConfiguration.getKey(),
                OpenAiChatCompletionRequest.makeRequest(request)
        );

        //Получения содержания сообщения
        String content = response.getChoices().get(0).getMessage().getContent();

        //Инициализация маппера
        ObjectMapper objectMapper = new ObjectMapper();
        //Конфигурация маппера для корректной обработки полей со значением false
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper.readValue(content, OpenAiAnalyzeResponseDto.class);
    }
}
