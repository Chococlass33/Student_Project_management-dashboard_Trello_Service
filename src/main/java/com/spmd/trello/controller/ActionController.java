package com.spmd.trello.controller;

import com.spmd.trello.BadConfig;
import com.spmd.trello.model.Action;
import com.spmd.trello.model.Board;
import com.spmd.trello.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


//TBD, currently stock placeholder from springboot tutorial until i get the entities right
@RestController
@CrossOrigin(origins = {"http://localhost:3002", BadConfig.FRONTEND_URL})
class ActionController
{
    @Autowired
    private final ActionRepository repository;
    @Autowired
    private final BoardRepository boardRepository;

    ActionController(ActionRepository repository, BoardRepository boardRepository)
    {
        this.repository = repository;
        this.boardRepository = boardRepository;
    }

    @GetMapping(path = "/actions")
    Iterable<Action> all(@RequestParam String boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow();
        return board.getActions();
    }

    @PostMapping("/action")
    Action newAction(@RequestBody Action newAction) {
        return repository.save(newAction);
    }

    // Single item

    @GetMapping("/action/{id}")
    Action one(@PathVariable String id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException());
    }

    @DeleteMapping("/action/{id}")
    void deleteAction(@PathVariable String id) {
        repository.deleteById(id);
    }
}