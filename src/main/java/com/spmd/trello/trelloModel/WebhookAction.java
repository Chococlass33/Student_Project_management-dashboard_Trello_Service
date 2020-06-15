package com.spmd.trello.trelloModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Represents an action POSTEed to the webhook
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookAction {
    private static final Logger logger = LoggerFactory.getLogger(WebhookAction.class);
    /**
     * The model that is being monitored
     */
    public Model model;
    /**
     * The action that was sent
     */
    public Action action;

    @Override
    public String toString() {
        return "WebhookAction{" +
                "model='" + model + '\'' +
                ", action='" + action + '\'' +
                '}';
    }

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

        @Override
        public String toString() {
            return "Model{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", closed=" + closed +
                    ", pos=" + pos +
                    ", idBoard='" + idBoard + '\'' +
                    '}';
        }

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
        public Object data;
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
}
