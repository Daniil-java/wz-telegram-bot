package com.education.wztelegrambot.services;

import com.education.wztelegrambot.dtos.OpenAiAnalyzeResponseDto;
import com.education.wztelegrambot.entities.HeaderData;
import com.education.wztelegrambot.entities.Order;
import com.education.wztelegrambot.entities.ProcessingStatus;
import com.education.wztelegrambot.entities.UserEntity;
import com.education.wztelegrambot.dtos.OrderWzDto;
import com.education.wztelegrambot.repositories.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {
    private final WzService wzService;
    private final OrderRepository orderRepository;
    private final HeadersService headersService;
    private final OpenAiService openAiService;


    public void fetchAndSaveEntity(UserEntity user) {
        HeaderData headerData = user.getAgreedHeader();
        try {
            List<OrderWzDto> orderWzDtoList = wzService.loadOrders(headerData);
            log.info("Count of load orders: {}", orderWzDtoList.size());
            if (orderWzDtoList != null) {
                for (OrderWzDto orderWzDto: orderWzDtoList) {
                    if (!orderRepository.findByWzId(orderWzDto.getId()).isPresent()) {
                        orderRepository.save(Order.convert(orderWzDto)
                                .setProcessingStatus(ProcessingStatus.CREATED)
                        );
                    }
                }
            }
        } catch (IOException e) {
            log.error("Orders update error!", e);
            headersService.increaseHeaderLoadAttemptCount(headerData);
        }

    }

    public Order analyzeAndUpdateOrder(Order order) throws JsonProcessingException {
        //Запрос к OpenAI на анализ заказа
        OpenAiAnalyzeResponseDto openAiAnalyzeResponseDto = openAiService.analyzeOrder(order);
        return orderRepository.save(order
                .setDevelopment(openAiAnalyzeResponseDto.isDevelopment())
                .setSolvableByAi(openAiAnalyzeResponseDto.isSolvableByAi())
                .setProcessingStatus(ProcessingStatus.ANALYZED)
        );
    }

    public List<Order> getAllNotAnalyzedOrders() {
        return orderRepository.findAllByProcessingStatus(ProcessingStatus.CREATED);
    }
}
