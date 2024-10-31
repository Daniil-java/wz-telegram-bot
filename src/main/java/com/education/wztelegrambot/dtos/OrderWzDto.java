package com.education.wztelegrambot.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderWzDto {
    private long id;
    private long categoryId;
    private String subject;
    private long customerId;
    private String description;
    private long duration;
    @JsonIgnore
    private List<Object> files;
    private long freelancerEarn;
    private boolean hasUnreadMark;
    private boolean isArchived;
    private boolean isChatClosed;

    @JsonProperty("isInsolvoOrder")
    private boolean isInsolvoOrder;

    @JsonProperty("isOfferOrder")
    private boolean isOfferOrder;

    @JsonProperty("isPremium")
    private boolean isPremium;

    @JsonProperty("isPremiumAvailable")
    private boolean isPremiumAvailable;

    @JsonProperty("isRaisePriceAvailable")
    private boolean isRaisePriceAvailable;

    @JsonProperty("isTestOptionSelected")
    private boolean isTestOptionSelected;

    @JsonProperty("isTinkoffOrder")
    private boolean isTinkoffOrder;

    @JsonProperty("isYooKassaOrder")
    private boolean isYooKassaOrder;

    private long modified;
    private long pinIdAtHistory;
    private long price;
    private boolean isShowAcceptTaskButton;
    private boolean isShowLeaveFeedbackButton;
    private boolean isShowOfferAcceptMessage;
    private long status;

    @Override
    public String toString() {
        return subject;
    }
}
