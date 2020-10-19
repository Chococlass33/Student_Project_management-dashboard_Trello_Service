package com.spmd.trello;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the current state of a trello list
 */
public class TrelloList {
    public String id;
    public String name;
    public float pos;
    public boolean closed;
    public Map<String, TrelloCard> cards = new HashMap<>();

    public TrelloList(String id, String name, float pos, boolean closed, Collection<TrelloCard> cards) {
        this.id = id;
        this.name = name;
        this.pos = pos;
        this.closed = closed;
        cards.forEach(card -> this.cards.put(card.id, card.clone()));
    }

    public TrelloList(RawBoard.RawList rawList, RawBoard.RawCard[] rawCards) {
        id = rawList.id;
        name = rawList.name;
        pos = rawList.pos;
        closed = rawList.closed;
        Arrays.stream(rawCards)
                .filter(rawCard -> rawCard.idList.equals(id))
                .forEach(rawCard -> cards.put(rawCard.id, new TrelloCard(rawCard)));
    }

    public TrelloList clone() {
        return new TrelloList(id, name, pos, closed, cards.values());
    }
}