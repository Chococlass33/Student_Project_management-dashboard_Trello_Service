package com.spmd.trello.controller;

import com.spmd.trello.model.Checklist;
import com.spmd.trello.repositories.ChecklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


//TBD, currently stock placeholder from springboot tutorial until i get the entities right
@RestController
class ChecklistController
{
    @Autowired
    private final ChecklistRepository repository;

    ChecklistController(ChecklistRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping(path = "/Checklists")
    Iterable<Checklist> all() {
        return repository.findAll();
    }

    @PostMapping("/Checklist")
    Checklist newChecklist(@RequestBody Checklist newChecklist) {
        return repository.save(newChecklist);
    }

    // Single item

    @GetMapping("/Checklist/{id}")
    Checklist one(@PathVariable String id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException());
    }

    @DeleteMapping("/Checklist/{id}")
    void deleteChecklist(@PathVariable String id) {
        repository.deleteById(id);
    }
}