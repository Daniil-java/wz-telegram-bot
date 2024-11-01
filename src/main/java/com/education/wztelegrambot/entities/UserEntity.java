package com.education.wztelegrambot.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private Long telegramId;
    private Long chatId;
    private String firstname;
    private String lastname;
    private String languageCode;
    @CreationTimestamp
    private LocalDateTime created;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<HeaderData> headers = new ArrayList<>();


    public HeaderData getAgreedHeader() {
        for (HeaderData headerData: headers) {
            if (headerData.getHeaderStatus().equals(HeaderStatus.OK)) {
                return headerData;
            }
        }
        return null;
    }

}
