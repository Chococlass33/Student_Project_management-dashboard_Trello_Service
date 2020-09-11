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

    @PostMapping("/CardLabels")
    CardLabel newCardLabel(@RequestBody CardLabel newCardLabel) {
        return repository.save(newCardLabel);
    }

    // Single item

    @GetMapping("/CardLabels/{id}")
    CardLabel one(@PathVariable Integer id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException());
    }

    @DeleteMapping("/CardLabels/{id}")
    void deleteCardLabel(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}