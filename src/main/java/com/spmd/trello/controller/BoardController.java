package com.spmd.trello.controller;

import com.spmd.trello.BadConfig;
import com.spmd.trello.model.Board;
import com.spmd.trello.repositories.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;


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
    boolean boardHistory(@PathVariable String id, @RequestParam("date") Optional<Date> date) {
        Optional<Board> retrievedBoard = repository.findById(id);

        boolean output = false;
        // If board and date are found: perform handleBoardHistory(), otherwise return BoardNotFoundException.
        output = retrievedBoard.isPresent() && date.isPresent() ? handleBoardHistory(retrievedBoard.get(), date.get()) : false;
        return output;
    }

    @DeleteMapping("/board/{id}")
    void deleteBoard(@PathVariable String id) {
        repository.deleteById(id);
    }

    private boolean handleBoardHistory(Board board, Date date) {
        // Now there is a valid board and date (which should be ISO8601 compliant from front-end).
        // Now we can use the date to handle and filter actions to a specific time.
        // Can't continue until method of reconstructing board is found (either through database entries of list/card etc..
        // Or through webhook actions and reconstructing through a series of actions.

        return true;
    }
}