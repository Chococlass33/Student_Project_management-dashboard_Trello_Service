package com.spmd.trello.controller;

import com.spmd.trello.model.CardMember;
import com.spmd.trello.repositories.CardMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


//TBD, currently stock placeholder from springboot tutorial until i get the entities right
@RestController
class CardMemberController
{
    @Autowired
    private final CardMemberRepository repository;

    CardMemberController(CardMemberRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping(path = "/CardMembers")
    Iterable<CardMember> all() {
        return repository.findAll();
    }

    @PostMapping("/CardMember")
    CardMember newCardMember(@RequestBody CardMember newCardMember) {
        return repository.save(newCardMember);
    }

    // Single item

    @GetMapping("/CardMember/{id}")
    CardMember one(@PathVariable String id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException());
    }

    @DeleteMapping("/CardMember/{id}")
    void deleteCardMember(@PathVariable String id) {
        repository.deleteById(id);
    }
}