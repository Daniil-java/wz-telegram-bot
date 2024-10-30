package com.education.wztelegrambot.entities.dtos;

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
    private boolean archived;
    private boolean chatClosed;

    @JsonProperty("isInsolvoOrder")
    private boolean insolvoOrder;

    @JsonProperty("isOfferOrder")
    private boolean offerOrder;

    @JsonProperty("isPremium")
    private boolean premium;

    @JsonProperty("isPremiumAvailable")
    private boolean premiumAvailable;

    @JsonProperty("isRaisePriceAvailable")
    private boolean raisePriceAvailable;

    @JsonProperty("isTestOptionSelected")
    private boolean testOptionSelected;

    @JsonProperty("isTinkoffOrder")
    private boolean tinkoffOrder;

    @JsonProperty("isYooKassaOrder")
    private boolean yooKassaOrder;

    private long modified;
    private long pinIdAtHistory;
    private long price;
    private boolean showAcceptTaskButton;
    private boolean showLeaveFeedbackButton;
    private boolean showOfferAcceptMessage;
    private long status;

    @Override
    public String toString() {
        return subject;
    }
}
