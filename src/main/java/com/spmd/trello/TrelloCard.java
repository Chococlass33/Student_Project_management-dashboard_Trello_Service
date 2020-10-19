package com.spmd.trello;

import com.spmd.trello.controller.HistoryController;

/**
 * Represents the current state of a trello card
 */
public class TrelloCard {
    public String id;
    public String desc;
    public String name;
    public float pos;
    public boolean closed;

    public TrelloCard(String id, String desc, String name, float pos, boolean closed) {
        this.id = id;
        this.desc = desc;
        this.name = name;
        this.pos = pos;
        this.closed = closed;
    }

    public TrelloCard(RawBoard.RawCard rawCard) {
        id = rawCard.id;
        desc = rawCard.desc;
        name = rawCard.name;
        pos = rawCard.pos;
        closed = rawCard.closed;
    }
}
