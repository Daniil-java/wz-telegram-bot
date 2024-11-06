package com.education.wztelegrambot.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HeaderStatus {
    OK,
    ERROR,
    REJECT;
}
