package com.spmd.trello.controller;

import com.spmd.trello.BadConfig;
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
import java.util.Locale;
import java.util.Optional;

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
    ResponseEntity<Board> boardHistory(@PathVariable String boardId, @RequestParam String token, @RequestParam("date") Optional<String> rawDate) {
        /* Get the current state of the board */
        URI url = UriComponentsBuilder.fromHttpUrl("https://api.trello.com/1/boards/" + boardId)
                .queryParam("key", BadConfig.API_KEY)
                .queryParam("token", token)
                .queryParam("fields", "name,dateLastActivity,shortUrl,name")
                .queryParam("lists", "open")
                .queryParam("cards", "open")
                .build().toUri();
        RestTemplate restTemplate = new RestTemplate();
        Board board = restTemplate.getForObject(url, Board.class);

        /* Check we have a board */
        if (board == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        /* If the date is empty, we can just return this state of the board */
        if (rawDate.isEmpty()) {
            return ResponseEntity.ok(board);
        }

        /* Get all the actions filtered by date */
        Date date;
        date = java.sql.Date.valueOf(rawDate.get());

        actionRepository.findAllByBoardAndDateCreated(boardId, date);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private static class Board {
        public String name;
        public Card[] cards;
        public TrelloList[] lists;
    }

    private static class Card {
        public String id;
        public String desc;
        public String idList;
        public String name;
        public float pos;
    }

    private static class TrelloList {
        public String id;
        public String name;
        public float pos;
    }
}
