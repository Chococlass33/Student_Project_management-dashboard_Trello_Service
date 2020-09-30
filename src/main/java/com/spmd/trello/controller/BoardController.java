package com.spmd.trello.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.spmd.trello.model.Action;
import com.spmd.trello.model.Board;
import com.spmd.trello.model.Card;
import com.spmd.trello.model.List;
import com.spmd.trello.repositories.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


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

    @GetMapping("/boards/{boardId}")
    Board getBoard(@PathVariable String boardId) {
        return repository.findById(boardId).orElse(null);
    }

    @GetMapping("/boardHistory/{id}")
    HistoryResponse boardHistory(@PathVariable String id, @RequestParam("date") Optional<String> date) {
        Optional<Board> retrievedBoard = repository.findById(id);
        if (date.isEmpty() && retrievedBoard.isPresent()) {

        }
        System.out.println("Enter with valid date...");
        //Convert date. (Use a temp date at the moment).
        try {
            Date ddate;
            if (date.isEmpty()) {
                ddate = new Date();
            } else {
                DateFormat format = new SimpleDateFormat("MMddyyyy", Locale.ENGLISH);
                ddate = format.parse(date.get());
            }
            System.out.println(ddate);
            // If board and date are found: perform handleBoardHistory(), otherwise return BoardNotFoundException (or original board).
            Board board = handleBoardHistory(retrievedBoard.get(), ddate);
            HistoryResponse output = new HistoryResponse();
            output.board = board;
            board.getLists().forEach(output::addList);
            return output;

        } catch (Exception e) {
            System.out.println("Exception encountered");
        }
        return null;
    }

    /**
     * A class for encoding the json response for the board history
     */
    private static class HistoryResponse {
        /**
         * The board
         */
        public Board board;
        /**
         * The lists in that board
         * Uses a subclass to encode the list data and the card data as a tuple-like
         */
        public Set<HistoryList> lists = new HashSet<>();

        /**
         * Class representing the combination of a list and it's cards
         */
        private static class HistoryList {
            public List list;
            public Set<Card> cards;
        }

        /**
         * Add a new list to the history
         * Records the list data and all the cards on that list
         *
         * @param list The list to add
         */
        public void addList(List list) {
            HistoryList historyList = new HistoryList();
            historyList.list = list;
            historyList.cards = list.getCards();
            lists.add(historyList);
        }
    }

    @DeleteMapping("/boards/{id}")
    void deleteBoard(@PathVariable String id) {
        repository.deleteById(id);
    }

    private Board handleBoardHistory(Board board, Date date) {
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
            JsonElement root = JsonParser.parseString(filteredAction.getData());

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

        return board;
    }

    private Set<List> filterListsWithDate(Date date, Set<List> lists) {
        return lists.stream() // Stream to let us do fancy things
                .filter(card -> new Date(card.getDateCreated().getTime()).before(date)) // Filter only those with the date
                .collect(Collectors.toSet()); // Turn into a set
    }

    private Set<Card> filterCardsWithDate(Date date, Set<Card> cards) {
        return cards.stream() // Stream to let us do fancy things
                .filter(card -> new Date(card.getDateCreated().getTime()).before(date)) // Filter only those with the date
                .collect(Collectors.toSet()); // Turn into a set
    }

    private Set<Action> filterActionsWithDate(Date date, Set<Action> actions) {
        return actions.stream() // Stream to let us do fancy things
                .filter(card -> new Date(card.getDateCreated().getTime()).before(date)) // Filter only those with the date
                .collect(Collectors.toSet()); // Turn into a set
    }

    private boolean hasCardMoved(Action action) {
        JsonElement root = JsonParser.parseString(action.getData());
        return action.getType().equals("updateCard") && (root.getAsJsonObject().get("listBefore").getAsJsonObject().get("id").getAsString()
                .equals(root.getAsJsonObject().get("listAfter").getAsJsonObject().get("id").getAsString()));
    }
}