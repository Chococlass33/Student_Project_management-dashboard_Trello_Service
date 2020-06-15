package com.spmd.trello.controller;

import com.spmd.trello.BadConfig;
import com.spmd.trello.model.Card;
import com.spmd.trello.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


//TBD, currently stock placeholder from springboot tutorial until i get the entities right
@RestController
class CardController
{
    @Autowired
    private final CardRepository repository;

    CardController(CardRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping(path = "/cards")
    Iterable<Card> all() {
        return repository.findAll();
    }

    @PostMapping("/card")
    Card newCard(@RequestBody Card newCard) {
        return repository.save(newCard);
    }

    // Single item

    @GetMapping("/card/{id}")
    Card one(@PathVariable String id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException());
    }

    @DeleteMapping("/card/{id}")
    void deleteCard(@PathVariable String id) {
        repository.deleteById(id);
    }
}