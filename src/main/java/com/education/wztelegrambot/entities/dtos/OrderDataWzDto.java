package com.education.wztelegrambot.entities.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OrderDataWzDto {
    @JsonProperty("data")
    private DataWrapper data;
    private int result;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataWrapper {
        private boolean haveMoreOrders;
        private List<OrderWzDto> interesting;
        private boolean isStaticOrders;
        private List<OrderWzDto> other;

    }


}
