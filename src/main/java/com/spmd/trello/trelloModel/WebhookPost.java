package com.spmd.trello.trelloModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookPost {
    public String description;
    public String callbackURL;
    public String idModel;

}
