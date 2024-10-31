package com.education.wztelegrambot.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_headers")
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class HeaderData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String cookies;
    private String agentId;
    @Column(name = "header_status")
    @Enumerated(EnumType.STRING)
    private HeaderStatus headerStatus;
    @Column(name = "load_attempt_count")
    private Integer loadAttemptCount;
    @CreationTimestamp
    private LocalDateTime created;

    public int increaseCount() {
        loadAttemptCount = loadAttemptCount == null ? 1 : loadAttemptCount + 1;
        return loadAttemptCount;
    }

}
