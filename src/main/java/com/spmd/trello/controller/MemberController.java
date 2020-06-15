package com.spmd.trello.controller;

import com.spmd.trello.model.Member;
import com.spmd.trello.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


//TBD, currently stock placeholder from springboot tutorial until i get the entities right
@RestController
class MemberController
{
    @Autowired
    private final MemberRepository repository;

    MemberController(MemberRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping(path = "/Members")
    Iterable<Member> all() {
        return repository.findAll();
    }

    @PostMapping("/Member")
    Member newMember(@RequestBody Member newMember) {
        return repository.save(newMember);
    }

    // Single item

    @GetMapping("/Member/{id}")
    Member one(@PathVariable String id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException());
    }

    @DeleteMapping("/Member/{id}")
    void deleteMember(@PathVariable String id) {
        repository.deleteById(id);
    }
}