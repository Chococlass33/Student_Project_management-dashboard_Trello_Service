package com.spmd.trello;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the current state of the trello board
 */
public class TrelloBoard {
    public String name;
    public Map<String, TrelloList> lists = new HashMap<>();

    public TrelloBoard(RawBoard rawBoard) {
        name = rawBoard.name;
        for (RawBoard.RawList rawList : rawBoard.lists) {
            lists.put(rawList.id, new TrelloList(rawList, rawBoard.cards));
        }
    }

    public TrelloBoard removeClosed() {
        lists.values().removeIf(list -> list.closed);// Remove the closed lists
        lists.values() // Get each of the lists
                .forEach(list -> list.cards.values() //  Get the cards in the list
                        .removeIf(trelloCard -> trelloCard.closed)); // Remove them from the list if they are closed
        return this;
    }
}
