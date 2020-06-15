package com.spmd.trello.controller;

import com.spmd.trello.model.OrganizationMember;
import com.spmd.trello.repositories.OrganizationMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


//TBD, currently stock placeholder from springboot tutorial until i get the entities right
@RestController
class OrganizationMemberController
{
    @Autowired
    private final OrganizationMemberRepository repository;

    OrganizationMemberController(OrganizationMemberRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping(path = "/OrganizationMembers")
    Iterable<OrganizationMember> all() {
        return repository.findAll();
    }

    @PostMapping("/OrganizationMember")
    OrganizationMember newOrganizationMember(@RequestBody OrganizationMember newOrganizationMember) {
        return repository.save(newOrganizationMember);
    }

    // Single item

    @GetMapping("/OrganizationMember/{id}")
    OrganizationMember one(@PathVariable String id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException());
    }

    @DeleteMapping("/OrganizationMember/{id}")
    void deleteOrganizationMember(@PathVariable String id) {
        repository.deleteById(id);
    }
}