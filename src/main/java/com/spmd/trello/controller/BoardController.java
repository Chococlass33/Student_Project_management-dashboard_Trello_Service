package com.spmd.trello.controller;

import com.spmd.trello.BadConfig;
import com.spmd.trello.model.Action;
import com.spmd.trello.model.Board;
import com.spmd.trello.model.Card;
import com.spmd.trello.model.List;
import com.spmd.trello.repositories.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


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

    @PostMapping("/board")
    Board newBoard(@RequestBody Board newBoard) {
        return repository.save(newBoard);
    }

    // Single item

    @GetMapping("/board/{id}")
    Board one(@PathVariable String id) {
        return repository.findById(id).orElseThrow();
    }

    @GetMapping("/board/{id}")
    Board boardHistory(@PathVariable String id, @RequestParam("date") Optional<Date> date) {
        Optional<Board> retrievedBoard = repository.findById(id);

        Board output;
        // If board and date are found: perform handleBoardHistory(), otherwise return BoardNotFoundException (or original board).
        output = retrievedBoard.isPresent() && date.isPresent()
                ? handleBoardHistory(retrievedBoard.get(), date.get()) : repository.findById(id).orElseThrow();
        return output;
    }

    @DeleteMapping("/board/{id}")
    void deleteBoard(@PathVariable String id) {
        repository.deleteById(id);
    }

    private Board handleBoardHistory(Board board, Date date) {
        // Now there is a valid board and date (which should be ISO8601 compliant from front-end).
        // Now we can use the date to handle and filter actions to a specific time.
        // Can't continue until method of reconstructing board is found (either through database entries of list/card etc..
        // Or through webhook actions and reconstructing through a series of actions.
        Set<Action> actions = board.getActions();
        Set<Action> filteredActions = new HashSet<>();

        // Filter actions.
        for (Action action: actions
             ) {
            // If the date of the action is before the requested date, then add the action to the filtered actions set.
            if (new Date(action.getDateCreated().getTime()).before(date)) {
                filteredActions.add(action);

            }
        }

        // To Do:
        // Filter cards.
        // Filter lists.
        Set<List> lists = board.getLists();
        Set<Card> cards = board.getCards();

        // Parse json string from action.data into object and use the data thereafter.

        // Now we have:
        // - the set of actions that can recreate the state of the board
        // - relevant lists of the board
        // - relevant cards of the board
        // Consider case where card is updated.
        for (Action filteredAction : filteredActions) {
            if (isCardMoved(filteredAction)) {
                // Set the new list of the card.
                for (Card card : cards) {
                    if (card.getId() == filteredAction.getData().card.id) {
                        card.setList(filteredAction.getData().listAfter);
                        break;
                    }
                }
            }
        }

        // Finally, after all modifications, set new list and cards to board, for eventual display.
        board.setLists(lists);
        board.setCards(cards);

        return board;
    }

    private boolean isCardMoved(Action action) {
        return action.getType() == "updateCard" ? action.getData().listBefore && action.getData().listAfter : false;
    }
}