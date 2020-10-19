package com.spmd.trello.controller;

import com.spmd.trello.BadConfig;
import com.spmd.trello.HistoryProcessor;
import com.spmd.trello.RawBoard;
import com.spmd.trello.TrelloBoard;
import com.spmd.trello.database.Action;
import com.spmd.trello.database.ActionRepository;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@RestController
public class BurndownController {
    @Autowired
    private final ActionRepository actionRepository;

    public BurndownController(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    @GetMapping("/data/burndown/{boardId}")
    ResponseEntity<Map<Instant, TrelloBoard>> boardHistory(@PathVariable String boardId, @RequestParam String token) {
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


        /* Get all the actions */
        List<Action> actions = actionRepository.findAllByBoard(boardId);

        if (actions.size() == 0) {
            // we have nothing
            return new ResponseEntity<>(HttpStatus.OK);
        }

        /* Get the oldest date */
        Instant oldestDate = actions.stream()
                .min(Comparator.comparing(Action::getDateCreated)) // Get the action with the smallest date
                .map(Action::getDateCreated) // Convert it into the date
                .orElseThrow() // We know there has to be one action, so we throw if this fails.
                .toInstant(); // Easier to work with

        /* Get the newest date */
        Instant newestDate = actions.stream()
                .max(Comparator.comparing(Action::getDateCreated)) // Get the action with the biggest date
                .map(Action::getDateCreated) // Convert it into the date
                .orElseThrow() // We know there has to be one action, so we throw if this fails.
                .toInstant(); // Easier to work with

        /* Chunk them up into days */
        Map<Instant, List<Action>> chunked = actions.stream()
                .sorted(Comparator.comparing(Action::getDateCreated)) // Sort them oldest first
                .collect(groupingBy(action -> action.getDateCreated().toInstant().truncatedTo(ChronoUnit.DAYS), toList()));

        /* Fill in the days there were no events. So that we have an idea of how long it has been */
        oldestDate.truncatedTo(ChronoUnit.DAYS);
        while (oldestDate.isAfter(newestDate)) {
            chunked.putIfAbsent(oldestDate, new ArrayList<>());
            oldestDate.plus(1, ChronoUnit.DAYS);
        }

        /* Process the actions, recording the board state each time */
        Map<Instant, TrelloBoard> boardStates = new HashMap<>();
        chunked.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // Sort them
                .forEachOrdered(entry -> {
                    boardStates.put(entry.getKey(), board.clone()); // Add the current board state
                    HistoryProcessor.processActions(board, entry.getValue()); //Process the actions that happened that day
                });

        boardStates.put(oldestDate, board); // Put the final board state


        return ResponseEntity.ok(boardStates);
    }

}
