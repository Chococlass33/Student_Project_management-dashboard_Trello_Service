package com.spmd.trello.controller;

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
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class HistoryController {
    private static final Logger logger = LoggerFactory.getLogger(com.spmd.trello.controller.HistoryController.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMddyyyy", Locale.ENGLISH);

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
        actions = actions.stream().filter(action -> action.getDateCreated().after(date)).collect(Collectors.toList());

        

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private static class TrelloBoard {
        public String name;
        public Map<String, TrelloList> lists = new HashMap<>();

        public TrelloBoard(RawBoard rawBoard) {
            name = rawBoard.name;
            for (RawBoard.RawList rawList : rawBoard.lists) {
                lists.put(rawList.id, new TrelloList(rawList, rawBoard.cards));
            }
        }

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
        }

    }

    private static class RawBoard {
        public String name;
        public RawCard[] cards;
        public RawList[] lists;

        private static class RawCard {
            public String id;
            public String desc;
            public String idList;
            public String name;
            public float pos;
        }

        private static class RawList {
            public String id;
            public String name;
            public float pos;
        }
    }


}
