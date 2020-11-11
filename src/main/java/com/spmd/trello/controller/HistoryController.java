package com.spmd.trello.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spmd.trello.BadConfig;
import com.spmd.trello.HistoryProcessor;
import com.spmd.trello.RawBoard;
import com.spmd.trello.TrelloBoard;
import com.spmd.trello.TrelloCard;
import com.spmd.trello.TrelloList;
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
import java.util.Comparator;
import java.util.List;
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

        HistoryProcessor.processActions(board, actions);

        return ResponseEntity.ok(board.removeClosed());
    }




}
