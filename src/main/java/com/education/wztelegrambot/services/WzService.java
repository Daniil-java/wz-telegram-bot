package com.education.wztelegrambot.services;

import com.education.wztelegrambot.entities.HeaderData;
import com.education.wztelegrambot.dtos.OrderDataWzDto;
import com.education.wztelegrambot.dtos.OrderWzDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class WzService {
    private final static String DEFAULT_URL = "https://client.work-zilla.com/api/order/v4/list/open?hideInsolvoOrders=false";

    //Загрузка заказов при помощи Jsoup
    public List<OrderWzDto> loadOrders(HeaderData headerData) throws IOException {
        if (headerData == null) {
            return null;
        }

        Map<String, String> headers = new HashMap<>();
        headers.put("agentid", headerData.getAgentId());
        headers.put("cookie", headerData.getCookies());

        //Настройка запроса
        Connection connection = Jsoup.connect(DEFAULT_URL)
                .headers(headers)
                .method(Connection.Method.GET)
                .ignoreContentType(true);
        //Выполнение запроса
        Document document = connection.get();

        String content = document.body().text();
        //Преобразование JSON в ДТО-объект
        OrderDataWzDto orderDataWzDto = new ObjectMapper().readValue(
                content, new TypeReference<OrderDataWzDto>() {});

        return Stream.concat(
                orderDataWzDto.getData().getInteresting().stream(),
                orderDataWzDto.getData().getOther().stream()
        ).collect(Collectors.toList());
    }
}
