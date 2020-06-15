package com.spmd.trello.controller;

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

    @GetMapping(path = "/Cards")
    Iterable<Card> all() {
        return repository.findAll();
    }

    @PostMapping("/Card")
    Card newCard(@RequestBody Card newCard) {
        return repository.save(newCard);
    }

    // Single item

    @GetMapping("/Card/{id}")
    Card one(@PathVariable String id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException());
    }

    @DeleteMapping("/Card/{id}")
    void deleteCard(@PathVariable String id) {
        repository.deleteById(id);
    }
}