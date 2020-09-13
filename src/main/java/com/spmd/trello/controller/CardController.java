package com.spmd.trello.controller;

import com.spmd.trello.model.Card;
import com.spmd.trello.model.Member;
import com.spmd.trello.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


//TBD, currently stock placeholder from springboot tutorial until i get the entities right
@RestController
class CardController {
    @Autowired
    private final CardRepository repository;

    CardController(CardRepository repository) {
        this.repository = repository;
    }

    @GetMapping(path = "/cards")
    Iterable<Card> all() {
        return repository.findAll();
    }

    @PostMapping("/cards")
    Card newCard(@RequestBody Card newCard) {
        return repository.save(newCard);
    }

    // Single item

    @GetMapping("/cards/{id}")
    Card one(@PathVariable String id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException());
    }

    @DeleteMapping("/cards/{id}")
    void deleteCard(@PathVariable String id) {
        repository.deleteById(id);
    }

}