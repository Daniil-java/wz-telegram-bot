package com.education.wztelegrambot.processors;

import com.education.wztelegrambot.entities.UserEntity;
import com.education.wztelegrambot.services.OrderService;
import com.education.wztelegrambot.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class OrdersCheckUpdateScheduleProcessor implements ScheduleProcessor {
    private final OrderService orderService;
    private final UserService userService;

    @Override
    public void process() {
        //Получение списка всех пользователей
        List<UserEntity> userEntityList = userService.getAll();

        for (UserEntity user: userEntityList) {
            //Обновление списка заказов
            orderService.loadAndSaveUserOrders(user);
        }

    }

    @Override
    public String getSchedulerName() {
        return this.getClass().getSimpleName();
    }
}
