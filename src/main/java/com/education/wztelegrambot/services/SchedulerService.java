package com.education.wztelegrambot.services;

import com.education.wztelegrambot.processors.NotificationScheduleProcessor;
import com.education.wztelegrambot.processors.OpenAiScheduleProcessor;
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
    private final OpenAiScheduleProcessor openAiScheduleProcessor;
    private final NotificationScheduleProcessor notificationScheduleProcessor;

//    @Scheduled(cron = "*/30 * * * * *")
    public void ordersCheckUpdateScheduleProcessor() {
        log.info(ordersCheckUpdateScheduleProcessor.getSchedulerName() + " started working!");
        ordersCheckUpdateScheduleProcessor.process();
    }

//    @Scheduled(cron = "*/45 * * * * *")
    public void openAiScheduleProcessor() {
        log.info(openAiScheduleProcessor.getSchedulerName() + "started working");
        openAiScheduleProcessor.process();
    }

    @Scheduled(cron = " */60 * * * * *")
    public void notificationScheduleProcessor() {
        log.info(notificationScheduleProcessor.getSchedulerName() + "started working");
        notificationScheduleProcessor.process();
    }
}
