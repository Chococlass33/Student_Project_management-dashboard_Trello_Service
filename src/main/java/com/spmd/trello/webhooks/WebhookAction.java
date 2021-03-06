package com.spmd.trello.webhooks;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.Map;

/**
 * Represents an action POSTEed to the webhook
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookAction {
    /**
     * The model that is being monitored
     */
    public Model model;
    /**
     * The action that was sent
     */
    public Action action;

    /**
     * Represents the model that the webhook is monitoring
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Model {
        public String id;
        public String name;
        public boolean closed;
        public float pos; //Float because it _can_ be .5 sometimes
        /**
         * The id of the board that this model is a part of.
         * If the model is the board itself, then this is null
         */
        private String idBoard;

        public String getIdBoard() {
            if (idBoard == null) {
                return id;
            } else {
                return idBoard;
            }
        }
    }

    /**
     * The action that was sent.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Action {
        public String id;
        /**
         * The data that the action contains
         * <p>
         * This is currently just a plain object because we don't actually care about it right now,
         * as it gets stored in the DB as a plain string anyway
         */
        public ActionData data;
        public Date date;
        public String idMemberCreator;
        /**
         * The type of the action.
         * This can be one of a number of different strings.
         * TODO: Make this an enum?
         */
        public String type;

        @Override
        public String toString() {
            return "Action{" +
                    "id='" + id + '\'' +
                    ", date=" + date +
                    ", idMemberCreator='" + idMemberCreator + '\'' +
                    ", type='" + type + '\'' +
                    ", data=" + data +
                    '}';
        }

        public String getData() {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.writeValueAsString(data);
            } catch (JsonProcessingException e) {
                return data.toString();
            }
        }
    }

    private static class ActionData {
        public Map<String, String> old;
        public IdObject card;
        public IdObject listAfter;
        public IdObject board;
        public IdObject list;

        private static class IdObject {
            public String id;
        }
    }
}
