package com.spmd.trello.trelloModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookResponse {
    public String description;
    public String callbackUrl;
    public String idModel;
    public boolean active;
    public int consecutiveFailures;
    public String firstConsecutiveFailDate;

    @Override
    public String toString() {
        return "WebhookResponse{" +
                "description='" + description + '\'' +
                ", callbackUrl='" + callbackUrl + '\'' +
                ", idModel='" + idModel + '\'' +
                ", active=" + active +
                ", consecutiveFailures=" + consecutiveFailures +
                ", firstConsecutiveFailDate='" + firstConsecutiveFailDate + '\'' +
                '}';
    }
}
