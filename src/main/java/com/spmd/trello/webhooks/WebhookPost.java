package com.spmd.trello.webhooks;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Request we POST to trello to make a new webhook
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookPost {
    public String description;
    public String callbackURL;
    public String idModel;

}
