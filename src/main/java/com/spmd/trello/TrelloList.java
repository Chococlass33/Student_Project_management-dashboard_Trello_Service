package com.spmd.trello;

import java.util.Arrays;
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

    public TrelloList(RawBoard.RawList rawList, RawBoard.RawCard[] rawCards) {
        id = rawList.id;
        name = rawList.name;
        pos = rawList.pos;
        closed = rawList.closed;
        Arrays.stream(rawCards)
                .filter(rawCard -> rawCard.idList.equals(id))
                .forEach(rawCard -> cards.put(rawCard.id, new TrelloCard(rawCard)));
    }
}