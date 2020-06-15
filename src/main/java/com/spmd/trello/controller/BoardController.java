package com.spmd.trello.controller;

import com.spmd.trello.BadConfig;
import com.spmd.trello.model.Board;
import com.spmd.trello.repositories.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


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

    @DeleteMapping("/board/{id}")
    void deleteBoard(@PathVariable String id) {
        repository.deleteById(id);
    }
}