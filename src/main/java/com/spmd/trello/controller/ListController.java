package com.spmd.trello.controller;

import com.spmd.trello.BadConfig;
import com.spmd.trello.model.List;
import com.spmd.trello.repositories.ListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


//TBD, currently stock placeholder from springboot tutorial until i get the entities right
@RestController
@CrossOrigin(origins = {"http://localhost:3002", BadConfig.FRONTEND_URL})
class ListController
{
    @Autowired
    private final ListRepository repository;

    ListController(ListRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping(path = "/lists")
    Iterable<List> all() {
        return repository.findAll();
    }

    @PostMapping("/list")
    List newList(@RequestBody List newList) {
        return repository.save(newList);
    }

    // Single item

    @GetMapping("/list/{id}")
    List one(@PathVariable String id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException());
    }

    @DeleteMapping("/list/{id}")
    void deleteList(@PathVariable String id) {
        repository.deleteById(id);
    }
}