package com.spmd.trello;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spmd.trello.database.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

public class HistoryProcessor {
    private static final Logger logger = LoggerFactory.getLogger(HistoryProcessor.class);
    /**
     * Process each action in turn
     * An action must be _undone_.
     * eg, creating a card means it should be deleted from the board
     *
     * @param board   The board to manipulate
     * @param actions The list of actions to process
     */
    public static void processActions(TrelloBoard board, List<Action> actions) {
        logger.info("Processing for board: " + board.name);
        for (Action action : actions) {
            JsonObject root = JsonParser.parseString(action.getData()).getAsJsonObject();
            logger.info(action.getType());
            switch (action.getType()) {
                case "updateCard":
                    processUpdateCard(board, root);
                    break;
                case "copyCard":
                case "createCard":
                    processCreateCard(board, root);
                    break;
                case "deleteCard":
                    processDeleteCard(board, root);
                    break;

                case "updateList":
                    processUpdateList(board, root);
                    break;
                case "createList":
                    processCreateList(board, root);
                    break;
                default:
                    logger.error("unknown action type");
            }
        }
        logger.info("done\n");
    }

    /**
     * A list was created, so we need to delete it
     *
     * @param board The board to modify
     * @param root  The data
     */
    private static void processCreateList(TrelloBoard board, JsonObject root) {
        String listId = root.getAsJsonObject("list").get("id").getAsString();
        board.lists.remove(listId);
    }

    /**
     * A list has been updated
     *
     * @param board The board to modify
     * @param root  The data
     */
    private static void processUpdateList(TrelloBoard board, JsonObject root) {
        String listId = root.getAsJsonObject("list").get("id").getAsString();
        TrelloList list = board.lists.get(listId);

        JsonObject actionData = root.getAsJsonObject("old");
        Set<String> keys = actionData.keySet();
        for (String key : keys) {
            logger.info(key);
            switch (key) {
                case "pos": // List position changed
                    list.pos = actionData.get(key).getAsFloat();
                    break;
                case "closed": // List was closed/opened
                    list.closed = actionData.get(key).getAsBoolean();
                case "name": // List name changed
                    list.name = actionData.get(key).getAsString();
                    break;
                default: // Unknown field
                    logger.info("unknown data field: " + key);
                    break;
            }
        }
    }

    /**
     * A card was deleted
     * So we need to create it
     *
     * @param board The board to modify
     * @param root  The data
     */
    private static void processDeleteCard(TrelloBoard board, JsonObject root) {
        String cardId = root.getAsJsonObject("card").get("id").getAsString();
        String listId = root.getAsJsonObject("list").get("id").getAsString();
        TrelloList list = board.lists.get(listId);
        // We don;t have any data, so we have to put in defaults
        TrelloCard newCard = new TrelloCard(cardId, "", "", 0, true);
        list.cards.put(cardId, newCard);
    }

    /**
     * A card was created
     * So we need to delete it
     *
     * @param board The board to modify
     * @param root  The data containing the details
     */
    private static void processCreateCard(TrelloBoard board, JsonObject root) {
        String cardId = root.getAsJsonObject("card").get("id").getAsString();
        String listId = root.getAsJsonObject("list").get("id").getAsString();
        board.lists.get(listId).cards.remove(cardId);
    }

    /**
     * A card was updated
     *
     * @param board The board to modify
     * @param root  The data containing the details
     */
    private static void processUpdateCard(TrelloBoard board, JsonObject root) {
        String cardId = root.getAsJsonObject("card").get("id").getAsString();
        String listId;
        if (!root.get("list").isJsonNull()) {
            listId = root.getAsJsonObject("list").get("id").getAsString();
        } else if (!root.get("listAfter").isJsonNull()) {
            listId = root.getAsJsonObject("listAfter").get("id").getAsString();
        } else {
            logger.error("Unable to find list id");
            return;
        }

        JsonObject actionData = root.getAsJsonObject("old");
        Set<String> keys = actionData.keySet();

        TrelloCard card = board.lists.get(listId).cards.get(cardId);
        for (String key : keys) {
            logger.info(key);
            switch (key) {
                case "pos": // Card moved in a list
                    card.pos = actionData.get(key).getAsFloat();
                    break;
                case "name": // Card name changed
                    card.name = actionData.get(key).getAsString();
                    break;
                case "desc": // Card description changed
                    card.desc = actionData.get(key).getAsString();
                    break;
                case "idList": // Card moved from one list to another
                    TrelloList priorList = board.lists.get(actionData.get(key).getAsString());
                    TrelloList currentList = board.lists.get(listId);
                    priorList.cards.put(card.id, card);
                    currentList.cards.remove(card.id);
                    break;
                case "closed": // Card was closed/opened
                    card.closed = actionData.get(key).getAsBoolean();
                    break;
                default:
                    logger.info("unknown data field: " + key);
                    break;
            }
        }
    }
}
