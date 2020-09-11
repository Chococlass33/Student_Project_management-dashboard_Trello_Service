package com.spmd.trello.controller;

import com.spmd.trello.model.BoardMembership;
import com.spmd.trello.repositories.BoardMembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


//TBD, currently stock placeholder from springboot tutorial until i get the entities right
@RestController
class BoardMembershipController
{
    @Autowired
    private final BoardMembershipRepository repository;

    BoardMembershipController(BoardMembershipRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping(path = "/BoardMemberships")
    Iterable<BoardMembership> all() {
        return repository.findAll();
    }

    @PostMapping("/BoardMemberships")
    BoardMembership newBoardMembership(@RequestBody BoardMembership newBoardMembership) {
        return repository.save(newBoardMembership);
    }

    // Single item

    @GetMapping("/BoardMemberships/{id}")
    BoardMembership one(@PathVariable String id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException());
    }

    @DeleteMapping("/BoardMemberships/{id}")
    void deleteBoardMembership(@PathVariable String id) {
        repository.deleteById(id);
    }
}