package com.spmd.trello.controller;

import com.spmd.trello.model.Organization;
import com.spmd.trello.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


//TBD, currently stock placeholder from springboot tutorial until i get the entities right
@RestController
class OrganizationController
{
    @Autowired
    private final OrganizationRepository repository;

    OrganizationController(OrganizationRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping(path = "/Organizations")
    Iterable<Organization> all() {
        return repository.findAll();
    }

    @PostMapping("/Organization")
    Organization newOrganization(@RequestBody Organization newOrganization) {
        return repository.save(newOrganization);
    }

    // Single item

    @GetMapping("/Organization/{id}")
    Organization one(@PathVariable String id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException());
    }

    @DeleteMapping("/Organization/{id}")
    void deleteOrganization(@PathVariable String id) {
        repository.deleteById(id);
    }
}