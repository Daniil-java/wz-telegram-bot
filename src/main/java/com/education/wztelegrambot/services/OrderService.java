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
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {
    private final WzService wzService;
    private final OrderRepository orderRepository;
    private final HeadersService headersService;
    private final OpenAiService openAiService;


    public void loadAndSaveUserOrders(UserEntity user) {
        //Получение работающих заголовков
        HeaderData headerData = user.getAgreedHeader();

        try {

            //Загрузка новых заказов
            List<OrderWzDto> orderWzDtoList = wzService.loadOrders(headerData);
            log.info("Count of load orders: {}", orderWzDtoList != null ? orderWzDtoList.size() : 0);

            if (orderWzDtoList != null) {

                //Обработка новых заказов
                for (OrderWzDto orderWzDto: orderWzDtoList) {

                    //Сохранение новых заказов, в случае отстутствия дубликатов
                    if (!orderRepository.findByWzIdAndUser(orderWzDto.getId(), user).isPresent()) {

                        orderRepository.save(Order.convert(orderWzDto)
                                .setProcessingStatus(ProcessingStatus.CREATED)
                                .setUser(user));
                    }
                }
            }
        } catch (IOException e) {
            log.error("Orders update error!", e);

            //Увеличение счетчика допустимых ошибок в хедере
            headersService.increaseHeaderLoadAttemptCount(headerData);
        }

    }

    public Order analyzeAndUpdateOrder(Order order) throws JsonProcessingException {
        //Запрос к OpenAI на анализ заказа
        OpenAiAnalyzeResponseDto openAiAnalyzeResponseDto = openAiService.analyzeOrder(order);

        order.setDevelopment(openAiAnalyzeResponseDto.getIsDevelopment())
                .setSolvableByAi(openAiAnalyzeResponseDto.getIsSolvableByAi())
                .setMatchingFilter(openAiAnalyzeResponseDto.getIsMatchingFilter());

        if (openAiAnalyzeResponseDto.getIsMatchingFilter()) {
            order.setProcessingStatus(ProcessingStatus.ANALYZED);
        } else {
            order.setProcessingStatus(ProcessingStatus.NOT_MATCHING_FILTER);
        }

        return orderRepository.save(order);
    }

    public List<Order> getOrdersForAnalysis() {
        return orderRepository.findAllByProcessingStatus(ProcessingStatus.CREATED);
    }

    public List<Order> getOrdersForNotification() {
        return orderRepository.findAllByProcessingStatus(ProcessingStatus.ANALYZED);
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public void setOrderAppliedById(long orderId) {
        orderRepository.updateStatusById(orderId, ProcessingStatus.APPLIED);
    }

    public void setOrderRejectedById(long orderId) {
        orderRepository.updateStatusById(orderId, ProcessingStatus.REJECTED);
    }

    public String fetchGenerateCoverLetter(long orderId, String info) {
        return orderRepository.findById(orderId)
                .map(order -> openAiService.generateCoverLetter(order, info))
                .orElse(null);
    }

    public void updateNotMatchingOrdersProcessingStatusByUser(UserEntity user) {
        LocalDateTime time = LocalDateTime.now().minusDays(1);
        orderRepository.updateOrderStatusByUserAndStatus(
                user,
                ProcessingStatus.NOT_MATCHING_FILTER,
                ProcessingStatus.ANALYZED,
                time
        );
    }
}
