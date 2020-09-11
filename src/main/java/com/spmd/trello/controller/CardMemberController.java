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

    @PostMapping("/CardMembers")
    CardMember newCardMember(@RequestBody CardMember newCardMember) {
        return repository.save(newCardMember);
    }

    // Single item

    @GetMapping("/CardMembers/{id}")
    CardMember one(@PathVariable Integer id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException());
    }

    @DeleteMapping("/CardMembers/{id}")
    void deleteCardMember(@PathVariable Integer id) {
        repository.deleteById(id);
    }
}