package com.spmd.trello.webhooks;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Request that is sent to us, to create a new webhook.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewWebhook {
    /**
     * The id of the board to track
     */
    public String idModel;
    /**
     * The trello auth token to use
     */
    public String token;
}
