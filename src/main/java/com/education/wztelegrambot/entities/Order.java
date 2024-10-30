package com.education.wztelegrambot.entities;

import com.education.wztelegrambot.entities.dtos.OrderWzDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long wzId;
    private long categoryId;
    private String subject;
    private long customerId;
    private String description;
    private long duration;
    private boolean archived;
    private boolean chatClosed;
    private long price;
    private long status;

    public static Order convert(OrderWzDto orderWzDto) {
        return new Order()
                .setWzId(orderWzDto.getId())
                .setCategoryId(orderWzDto.getCategoryId())
                .setSubject(orderWzDto.getSubject())
                .setCustomerId(orderWzDto.getCustomerId())
                .setDescription(orderWzDto.getDescription())
                .setDuration(orderWzDto.getDuration())
                .setArchived(orderWzDto.isArchived())
                .setChatClosed(orderWzDto.isChatClosed())
                .setPrice(orderWzDto.getPrice())
                .setStatus(orderWzDto.getStatus());
    }
}