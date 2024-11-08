package com.education.wztelegrambot.processors;

import com.education.wztelegrambot.entities.Order;
import com.education.wztelegrambot.services.OrderService;
import com.education.wztelegrambot.telegram.utils.ThreadUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OpenAiScheduleProcessor implements ScheduleProcessor {
    private final OrderService orderService;
    //Максимально допустимое число исключений
    private final static int MAX_EXCEPTION = 5;
    @Override
    public void process() {
        //Загрузка всех необработанных OpenAI заказов
        List<Order> orderList = orderService.getOrdersForAnalysis();
        log.info("Count of unanalyzed orders: {}", orderList.size());

        //Счетчик выброшенных исключений
        int countException = 0;
        for (Order order: orderList) {
            if (countException > MAX_EXCEPTION) {
                log.error("Terminated due to errors!");
                break;
            }
            try {
                //Обработка заказа и сохранение
                orderService.analyzeAndUpdateOrder(order);
                ThreadUtil.sleep(100);
            } catch (JsonProcessingException e) {
                log.error("OpenAI Error!");
            }
        }
    }

    @Override
    public String getSchedulerName() {
        return this.getClass().getSimpleName();
    }
}
