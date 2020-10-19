package com.spmd.trello.webhooks;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response sent by trello upon creating a webhook
 */
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
