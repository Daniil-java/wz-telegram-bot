package com.education.wztelegrambot.services;

import com.education.wztelegrambot.entities.HeaderData;
import com.education.wztelegrambot.entities.Order;
import com.education.wztelegrambot.entities.UserEntity;
import com.education.wztelegrambot.dtos.OrderWzDto;
import com.education.wztelegrambot.repositories.OrderRepository;
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


    public void fetchAndSaveEntity(UserEntity user) {
        HeaderData headerData = user.getAgreedHeader();
        try {
            List<OrderWzDto> orderWzDtoList = wzService.loadOrders(headerData);
            if (orderWzDtoList != null) {
                for (OrderWzDto orderWzDto: orderWzDtoList) {
                    if (!orderRepository.findByWzId(orderWzDto.getId()).isPresent()) {
                        orderRepository.save(Order.convert(orderWzDto));
                        log.info(orderWzDto.getSubject());
                    }
                }
            }
        } catch (IOException e) {
            log.error("Orders update error!", e);
            headersService.increaseHeaderLoadAttemptCount(headerData);
        }

    }
}
