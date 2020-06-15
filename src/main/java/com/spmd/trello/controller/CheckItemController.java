package com.spmd.trello.controller;

import com.spmd.trello.model.CheckItem;
import com.spmd.trello.repositories.CheckItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


//TBD, currently stock placeholder from springboot tutorial until i get the entities right
@RestController
class CheckItemController
{
    @Autowired
    private final CheckItemRepository repository;

    CheckItemController(CheckItemRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping(path = "/CheckItems")
    Iterable<CheckItem> all() {
        return repository.findAll();
    }

    @PostMapping("/CheckItem")
    CheckItem newCheckItem(@RequestBody CheckItem newCheckItem) {
        return repository.save(newCheckItem);
    }

    // Single item

    @GetMapping("/CheckItem/{id}")
    CheckItem one(@PathVariable String id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException());
    }

    @DeleteMapping("/CheckItem/{id}")
    void deleteCheckItem(@PathVariable String id) {
        repository.deleteById(id);
    }
}