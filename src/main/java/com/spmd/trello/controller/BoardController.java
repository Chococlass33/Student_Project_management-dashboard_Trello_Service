package com.spmd.trello.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.spmd.trello.BadConfig;
import com.spmd.trello.model.Action;
import com.spmd.trello.model.Board;
import com.spmd.trello.model.Card;
import com.spmd.trello.model.List;
import com.spmd.trello.repositories.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;


//TBD, currently stock placeholder from springboot tutorial until i get the entities right
@RestController
class BoardController {
    @Autowired
    private final BoardRepository repository;

    BoardController(BoardRepository repository) {
        this.repository = repository;
    }

    @GetMapping(path = "/boards")
    Iterable<Board> all() {
        return repository.findAll();
    }

    @PostMapping("/boards")
    Board newBoard(@RequestBody Board newBoard) {
        return repository.save(newBoard);
    }

    // Single item

//    @GetMapping("/board/{id}")
//    Board one(@PathVariable String id) {
//        return repository.findById(id).orElseThrow();
//    }

    @GetMapping("/boards/{boardId}")
    Board getBoard(@PathVariable String boardId) {
        return repository.findById(boardId).orElseThrow();
    }

    @GetMapping("/boardHistory/{id}")
    Map boardHistory(@PathVariable String id, @RequestParam("date") String date) {
        Optional<Board> retrievedBoard = repository.findById(id);
        if (date.isEmpty() && retrievedBoard.isPresent()) {
            return null;
        }
        System.out.println("Enter with valid date...");
        Map output;
        //Convert date. (Use a temp date at the moment).
        try {
                DateFormat format = new SimpleDateFormat("MMddyyyy", Locale.ENGLISH);
            Date ddate = format.parse(date);
            // If board and date are found: perform handleBoardHistory(), otherwise return BoardNotFoundException (or original board).
            output = handleBoardHistory(retrievedBoard.get(), ddate);
            return output;

        } catch (Exception e) {
            System.out.println("Exception encountered");
        }
        return null;
    }

    @DeleteMapping("/boards/{id}")
    void deleteBoard(@PathVariable String id) {
        repository.deleteById(id);
    }

    private Map handleBoardHistory(Board board, Date date) {
        // Actions, cards, and lists.
        System.out.println("Entered handleBoardHistory...");
        Set<Action> actions = board.getActions();
        Set<Card> cards = board.getCards();
        Set<List> lists = board.getLists();

        // Filter actions, cards, and lists based on a given date.
        Set<Action> filteredActions = filterActionsWithDate(date, actions);
        Set<Card> filteredCards = filterCardsWithDate(date, cards);
        Set<List> filteredLists = filterListsWithDate(date, lists);


        // Create HashMap for cards and lists for performance.
        HashMap<String, Card> cardMap = new HashMap<>();
        for (Card filteredCard : filteredCards) {
            cardMap.put(filteredCard.getId(), filteredCard);
        }

        HashMap<String, List> listMap = new HashMap<>();

        for (List filteredList : filteredLists) {
            listMap.put(filteredList.getId(), filteredList);
        }

        // Perform card update functionality.
        for (Action filteredAction : filteredActions) {
            JsonElement root = new JsonParser().parse(filteredAction.getData());

            if (hasCardMoved(filteredAction)) {
                // Set the new list of the card.
                cardMap.get(root.getAsJsonObject().get("card").getAsJsonObject().get("id").getAsString()).setList(listMap.get(root.getAsJsonObject().get("listAfter").getAsString()));
            }
        }

        // Testing print statements.
        System.out.println(actions);
        System.out.println(cards);
        System.out.println(lists);
        System.out.println(cardMap);

        // Finally, after all modifications, set new list and cards to board, for eventual display.
        board.setLists(filteredLists);
        board.setCards(filteredCards);

        return board.frontEndData();
    }

    private Set<List> filterListsWithDate(Date date, Set<List> lists) {
        Set<List> filteredLists = new HashSet<>();
        // Filter lists.
        for (List list: lists) {
            // If the date of creation of the list is before the requested date, then add the list to the filtered lists set.
            if (new Date(list.getDateCreated().getTime()).before(date)) {
                filteredLists.add(list);
            }
        }

        return filteredLists;
    }

    private Set<Card> filterCardsWithDate(Date date, Set<Card> cards) {
        Set<Card> filteredCards = new HashSet<>();
        // Filter cards.
        for (Card card: cards) {
            // If the date of creation of the card is before the requested date, then add the card to the filtered cards set.
            if (new Date(card.getDateCreated().getTime()).before(date)) {
                filteredCards.add(card);
            }
        }

        return filteredCards;
    }

    private Set<Action> filterActionsWithDate(Date date, Set<Action> actions) {
        Set<Action> filteredActions = new HashSet<>();
        // Filter actions.
        for (Action action: actions) {
            // If the date of the action is before the requested date, then add the action to the filtered actions set.
            if (new Date(action.getDateCreated().getTime()).before(date)) {
                filteredActions.add(action);
            }
        }

        return filteredActions;
    }

    private boolean hasCardMoved(Action action) {
        JsonElement root = new JsonParser().parse(action.getData());
        return action.getType() == "updateCard" && (root.getAsJsonObject().get("listBefore").getAsJsonObject().get("id").getAsString()
                .equals(root.getAsJsonObject().get("listAfter").getAsJsonObject().get("id").getAsString()));
    }
}