package com.education.wztelegrambot.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OpenAiAnalyzeResponseDto {
    private Boolean isMatchingFilter;
    private Boolean isDevelopment;
    private Boolean isSolvableByAi;
}
