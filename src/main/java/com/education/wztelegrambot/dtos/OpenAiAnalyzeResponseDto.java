package com.education.wztelegrambot.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OpenAiAnalyzeResponseDto {
    @JsonProperty("isDevelopment")
    private boolean isDevelopment;
    @JsonProperty("isSolvableByAi")
    private boolean isSolvableByAi;
}
