package com.education.wztelegrambot.entities;

import lombok.RequiredArgsConstructor;
import lombok.Getter;

@RequiredArgsConstructor
@Getter
public enum ProcessingStatus {
    CREATED,
    ANALYZED,
    NOTIFICATED,
    NOTIFICATION_ERROR,
    APPLIED,
    REJECTED,
    NOT_MATCHING_FILTER;
}
