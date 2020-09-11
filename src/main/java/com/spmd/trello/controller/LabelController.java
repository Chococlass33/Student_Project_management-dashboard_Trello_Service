package com.spmd.trello.controller;

import com.spmd.trello.model.Label;
import com.spmd.trello.repositories.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


//TBD, currently stock placeholder from springboot tutorial until i get the entities right
@RestController
class LabelController
{
    @Autowired
    private final LabelRepository repository;

    LabelController(LabelRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping(path = "/Labels")
    Iterable<Label> all() {
        return repository.findAll();
    }

    @PostMapping("/Labels")
    Label newLabel(@RequestBody Label newLabel) {
        return repository.save(newLabel);
    }

    // Single item

    @GetMapping("/Labels/{id}")
    Label one(@PathVariable String id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException());
    }

    @DeleteMapping("/Labels/{id}")
    void deleteLabel(@PathVariable String id) {
        repository.deleteById(id);
    }
}