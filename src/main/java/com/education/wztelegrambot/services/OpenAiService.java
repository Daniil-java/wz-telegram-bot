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
                        "Тебе нужно ответить на 3 вопроса, по данному заказу. " +
                        "1: Соответствует ли заказ фильтру (JSON: 'isMatchingFilter : boolean') " +
                        "Если фильтр пустой, он должен быть отмечен, как true" +
                        "2: Относится ли данный заказ к IT, разработке. (JSON: 'isDevelopment : boolean')" +
                        "3. Сможешь ли ты, самостоятельно выполнить этот заказ. (JSON: 'isSolvableByAi : boolean')\n" +
                        "Фильтр: %s\n" +
                        "Название заказа: %s\n" +
                        "Описание: %s", order.getUser().getFilter(), order.getSubject(), order.getDescription());

        return fetchResponseAndReadJson(request, OpenAiAnalyzeResponseDto.class);

    }

    public String generateCoverLetter(Order order, String userInfo) {
        //Создание текста запроса для OpenAI
        String request = String.format(
                "Я нашёл задачу на сайте для фриланса. Я отправлю тебе название и описание задачи, а также информацию о себе, своих навыках и опыте. \n" +
                        "Я хочу, чтобы ты составил сопроводительное письмо, от моего имени - заказчику. В этом письме ты должен поздороваться, кратко представить меня и задать вопросы по задаче, если в описании не хватает информации. \n" +
                        "После письма напиши уже от себя, на что мне стоит обратить внимание. Например на возможность незаконной деятельности, при выполнении данной задачи или что-то другое.\n" +
                        "Название: %s\n" +
                        "Описание: %s\n" +
                        "Длительность в милисек.: %s" +
                        "Награда за задание в руб: %s" +
                        "Обо мне: %s",
                order.getSubject(), order.getDescription(), order.getDuration(), order.getPrice(), userInfo);

        return fetchResponse(request);
    }

    public String analyzeFilter(String filter) {
        String request = String.format(
                "В будущем, я буду отправлять тебе задачи и их описания. " +
                        "Сейчас я отправлю тебе фильтр, которому должны соответствовать задания. \n" +
                        "%s\n" +
                        "Опиши как ты понял фильтр. Какие задачи ты будешь считать соответствующими фильтру",
                filter
        );

        return fetchResponse(request);
    }

    //Отправка запроса и получение ответа OpenAI
    private String fetchResponse(String request) {
        return getContent(openAiFeignClient.generate(
                "Bearer " + openAiKeyConfiguration.getKey(),
                OpenAiChatCompletionRequest.makeRequest(request)
        ));
    }

    // Читаем JSON в указанный тип T
    private<T> T fetchResponseAndReadJson(String request, Class<T> responseType) throws JsonProcessingException {
        return new ObjectMapper().readValue(fetchResponse(request), responseType);
    }


    //Получения содержания сообщения
    public static String getContent(OpenAiChatCompletionResponse response) {
        return OpenAiChatCompletionResponse.getContent(response);
    }
}
