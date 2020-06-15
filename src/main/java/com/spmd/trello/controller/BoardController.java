package com.spmd.trello.controller;

import com.spmd.trello.model.Board;
import com.spmd.trello.repositories.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


//TBD, currently stock placeholder from springboot tutorial until i get the entities right
@RestController
class BoardController
{
    @Autowired
    private final BoardRepository repository;

    BoardController(BoardRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping(path = "/Boards")
    Iterable<Board> all() {
        return repository.findAll();
    }

    @PostMapping("/Board")
    Board newBoard(@RequestBody Board newBoard) {
        return repository.save(newBoard);
    }

    // Single item

    @GetMapping("/Board/{id}")
    Board one(@PathVariable String id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException());
    }

    @DeleteMapping("/Board/{id}")
    void deleteBoard(@PathVariable String id) {
        repository.deleteById(id);
    }
}