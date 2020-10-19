package com.spmd.trello.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spmd.trello.BadConfig;
import com.spmd.trello.database.Action;
import com.spmd.trello.database.ActionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.sql.Date;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class HistoryController {
    private static final Logger logger = LoggerFactory.getLogger(com.spmd.trello.controller.HistoryController.class);

    @Autowired
    private final ActionRepository actionRepository;

    HistoryController(ActionRepository repository) {
        this.actionRepository = repository;
    }

    @GetMapping("/history/{boardId}")
    ResponseEntity<TrelloBoard> boardHistory(@PathVariable String boardId, @RequestParam String token, @RequestParam("date") Optional<String> rawDate) {
        /* Get the current state of the board */
        URI url = UriComponentsBuilder.fromHttpUrl("https://api.trello.com/1/boards/" + boardId)
                .queryParam("key", BadConfig.API_KEY)
                .queryParam("token", token)
                .queryParam("fields", "name,dateLastActivity,shortUrl,name")
                .queryParam("lists", "all")
                .queryParam("cards", "all")
                .build().toUri();
        RestTemplate restTemplate = new RestTemplate();
        RawBoard rawBoard = restTemplate.getForObject(url, RawBoard.class);

        /* Check we have a board */
        if (rawBoard == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        TrelloBoard board = new TrelloBoard(rawBoard);

        /* If the date is empty, we can just return this state of the board */
        if (rawDate.isEmpty()) {
            return ResponseEntity.ok(board.removeClosed());
        }

        /* Get all the actions filtered by date */
        Date date = Date.valueOf(rawDate.get());
        List<Action> actions = actionRepository.findAllByBoard(boardId);
        actions = actions.stream().filter(action -> action.getDateCreated().after(date))
                .sorted(Comparator.comparing(Action::getDateCreated).reversed())
                .collect(Collectors.toList());

        processActions(board, actions);

        return ResponseEntity.ok(board.removeClosed());
    }

    /**
     * Process each action in turn
     * An action must be _undone_.
     * eg, creating a card means it should be deleted from the board
     *
     * @param board   The board to manipulate
     * @param actions The list of actions to process
     */
    private void processActions(TrelloBoard board, List<Action> actions) {
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
    private void processCreateList(TrelloBoard board, JsonObject root) {
        String listId = root.getAsJsonObject("list").get("id").getAsString();
        board.lists.remove(listId);
    }

    /**
     * A list has been updated
     *
     * @param board The board to modify
     * @param root  The data
     */
    private void processUpdateList(TrelloBoard board, JsonObject root) {
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
    private void processDeleteCard(TrelloBoard board, JsonObject root) {
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
    private void processCreateCard(TrelloBoard board, JsonObject root) {
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
    private void processUpdateCard(TrelloBoard board, JsonObject root) {
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

    /**
     * Represents the current state of the trello board
     */
    private static class TrelloBoard {
        public String name;
        public Map<String, TrelloList> lists = new HashMap<>();

        public TrelloBoard(RawBoard rawBoard) {
            name = rawBoard.name;
            for (RawBoard.RawList rawList : rawBoard.lists) {
                lists.put(rawList.id, new TrelloList(rawList, rawBoard.cards));
            }
        }

        TrelloBoard removeClosed() {
            lists.values().removeIf(list -> list.closed);// Remove the closed lists
            lists.values() // Get each of the lists
                    .forEach(list -> list.cards.values() //  Get the cards in the list
                            .removeIf(trelloCard -> trelloCard.closed)); // Remove them from the list if they are closed
            return this;
        }
    }

    /**
     * Represents the current state of a trello list
     */
    private static class TrelloList {
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

    /**
     * Represents the current state of a trello card
     */
    private static class TrelloCard {
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


    /**
     * The raw trello board data returned from the api
     */
    private static class RawBoard {
        public String name;
        public RawCard[] cards;
        public RawList[] lists;

        /**
         * The raw trello care data returned from the api
         */
        private static class RawCard {
            public String id;
            public String desc;
            public String idList;
            public String name;
            public float pos;
            public boolean closed;
        }

        /**
         * The raw trello list data returned from the api
         */
        private static class RawList {
            public String id;
            public boolean closed;
            public String name;
            public float pos;
        }
    }


}
