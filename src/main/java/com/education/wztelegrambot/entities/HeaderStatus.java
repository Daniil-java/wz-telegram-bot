package com.education.wztelegrambot.entities;

import lombok.RequiredArgsConstructor;
import lombok.Getter;

@RequiredArgsConstructor
@Getter
public enum HeaderStatus {
    OK,
    ERROR,
    REJECT;
}
