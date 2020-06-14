package com.spmd.trello.trelloModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookAction {
    public Model model;
    public Action action;

    @Override
    public String toString() {
        return "WebhookAction{" +
                "model='" + model + '\'' +
                ", action='" + action + '\'' +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Model {
        public String id;
        public String name;
        public boolean closed;
        public float pos; //Float because it _can_ be .5 sometimes
        public String idBoard;

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
    }

    public static class Action {
        public String id;
        public Object data;
        public Date date;
        public String idMemberCreator;
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
    }
}
