package com.spmd.trello.controller;

import com.spmd.trello.model.CardLabel;
import com.spmd.trello.repositories.CardLabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


//TBD, currently stock placeholder from springboot tutorial until i get the entities right
@RestController
class CardLabelController
{
    @Autowired
    private final CardLabelRepository repository;

    CardLabelController(CardLabelRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping(path = "/CardLabels")
    Iterable<CardLabel> all() {
        return repository.findAll();
    }

    @PostMapping("/CardLabel")
    CardLabel newCardLabel(@RequestBody CardLabel newCardLabel) {
        return repository.save(newCardLabel);
    }

    // Single item

    @GetMapping("/CardLabel/{id}")
    CardLabel one(@PathVariable String id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException());
    }

    @DeleteMapping("/CardLabel/{id}")
    void deleteCardLabel(@PathVariable String id) {
        repository.deleteById(id);
    }
}