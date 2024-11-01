package com.education.wztelegrambot.entities;

import com.education.wztelegrambot.dtos.OrderWzDto;
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
    private boolean isDevelopment;
    private boolean isSolvableByAi;
    @Enumerated(EnumType.STRING)
    private ProcessingStatus processingStatus;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @Column(name = "notification_attempt_count")
    private int notificationAttemptCount;

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
