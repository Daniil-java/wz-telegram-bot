package com.education.wztelegrambot.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProcessingStatus {
    CREATED,
    ANALYZED,
    NOTIFICATED,
    NOTIFICATION_ERROR;
}
