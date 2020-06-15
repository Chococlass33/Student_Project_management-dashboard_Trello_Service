package com.spmd.trello.controller;

import com.spmd.trello.model.Action;
import com.spmd.trello.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//TBD, currently stock placeholder from springboot tutorial until i get the entities right
@RestController
class ActionController
{
    @Autowired
    private final ActionRepository repository;

    ActionController(ActionRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping(path = "/actions")
    Iterable<Action> all() {
        return repository.findAll();
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