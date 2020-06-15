package com.spmd.trello.trelloModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NewWebhook {
    public String idModel;
    public String token;
}
