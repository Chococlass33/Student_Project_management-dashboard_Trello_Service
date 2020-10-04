package com.spmd.trello.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spmd.trello.BadConfig;
import com.spmd.trello.model.Action;
import com.spmd.trello.repositories.ActionRepository;
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
                .queryParam("lists", "open")
                .queryParam("cards", "open")
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
            return ResponseEntity.ok(board);
        }

        /* Get all the actions filtered by date */
        Date date = Date.valueOf(rawDate.get());
        List<Action> actions = actionRepository.findAllByBoard(boardId);
        actions = actions.stream().filter(action -> action.getDateCreated().after(date))
                .filter(action -> action.getType().equals("updateCard"))
                .sorted(Comparator.comparing(Action::getDateCreated).reversed())
                .collect(Collectors.toList());

        processActions(board, actions);

        return new ResponseEntity<>(board, HttpStatus.OK);
    }

    private void processActions(TrelloBoard board, List<Action> actions) {
        for (Action action : actions) {
            JsonObject root = JsonParser.parseString(action.getData()).getAsJsonObject();
            if (!root.has("card") || !root.has("list")) {
                // We don't know what card was affected
                continue;
            }
            String cardId = root.getAsJsonObject("card").get("id").getAsString();
            String listId;
            if (!root.get("list").isJsonNull()) {
                listId = root.getAsJsonObject("list").get("id").getAsString();
            } else if (!root.get("listAfter").isJsonNull()) {
                listId = root.getAsJsonObject("listAfter").get("id").getAsString();
            } else {
                logger.error("Unable to find list id");
                continue;
            }

            JsonObject actionData = root.getAsJsonObject("old");
            Set<String> keys = actionData.keySet();

            TrelloCard card = board.lists.get(listId).cards.get(cardId);
            for (String key : keys) {
                logger.info("Key: " + key + " Val: " + actionData.get(key).getAsString());
                switch (key) {
                    case "pos":
                        card.pos = actionData.get(key).getAsFloat();
                        break;
                    case "name":
                        card.name = actionData.get(key).getAsString();
                        break;
                    case "desc":
                        card.desc = actionData.get(key).getAsString();
                        break;
                    case "idList":
                        TrelloList priorList = board.lists.get(actionData.get(key).getAsString());
                        TrelloList currentList = board.lists.get(listId);
                        priorList.cards.put(card.id, card);
                        currentList.cards.remove(card.id);
                        break;
                    default:
                        break;
                }
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
    }

    /**
     * Represents the current state of a trello list
     */
    private static class TrelloList {
        public String id;
        public String name;
        public float pos;
        public Map<String, TrelloCard> cards = new HashMap<>();

        public TrelloList(RawBoard.RawList rawList, RawBoard.RawCard[] rawCards) {
            id = rawList.id;
            name = rawList.name;
            pos = rawList.pos;
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

        public TrelloCard(RawBoard.RawCard rawCard) {
            id = rawCard.id;
            desc = rawCard.desc;
            name = rawCard.name;
            pos = rawCard.pos;
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
        }

        /**
         * The raw trello list data returned from the api
         */
        private static class RawList {
            public String id;
            public String name;
            public float pos;
        }
    }


}
