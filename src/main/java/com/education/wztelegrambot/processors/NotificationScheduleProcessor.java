package com.education.wztelegrambot.processors;

import com.education.wztelegrambot.entities.Order;
import com.education.wztelegrambot.entities.ProcessingStatus;
import com.education.wztelegrambot.services.OrderService;
import com.education.wztelegrambot.services.TelegramService;
import com.education.wztelegrambot.telegram.utils.ThreadUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class NotificationScheduleProcessor implements ScheduleProcessor {
    private final OrderService orderService;
    private final TelegramService telegramService;
    //Допустимое количество ошибок при отправлении
    private static final int MAX_ATTEMPT_COUNT = 3;
    @Override
    public void process() {
        //Получения списка всех проанализированных заказов
        List<Order> orderList = orderService.getOrdersForNotification();

        for (Order order: orderList) {
            //Отправка сообщения пользователю и обработка результата
            if (telegramService.sendOrder(order) != null) {
                //Обработка, в случае удачного отправления сообщения
                order.setProcessingStatus(ProcessingStatus.NOTIFICATED);
            } else {
                //Обработка, в случае неудачи
                //Увеличение счетчика отправленных сообщений, по данному заказу
                order.setNotificationAttemptCount(order.getNotificationAttemptCount() + 1);

                //Изменение статуса обработки заказа,
                // в случае достижения предельно допустимого количества ошибок
                if (order.getNotificationAttemptCount() > MAX_ATTEMPT_COUNT) {
                    order.setProcessingStatus(ProcessingStatus.NOTIFICATION_ERROR);
                    log.error("Notification error!");
                }
            }
            //Сохранение состояния заказа
            orderService.save(order);
            ThreadUtil.sleep(100);
        }

    }

    @Override
    public String getSchedulerName() {
        return this.getClass().getSimpleName();
    }
}
