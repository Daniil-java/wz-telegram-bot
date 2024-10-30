package com.education.wztelegrambot.services;

import com.education.wztelegrambot.processors.OrdersCheckUpdateScheduleProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class SchedulerService {
    private final OrdersCheckUpdateScheduleProcessor ordersCheckUpdateScheduleProcessor;

    @Scheduled(cron = "*/30 * * * * *")
    public void ordersCheckUpdateScheduleProcessor() {
        log.info(ordersCheckUpdateScheduleProcessor.getSchedulerName() + " started working");
        ordersCheckUpdateScheduleProcessor.process();
    }
}
