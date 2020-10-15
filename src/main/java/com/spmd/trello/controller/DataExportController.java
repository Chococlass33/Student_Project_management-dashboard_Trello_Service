package com.spmd.trello.controller;
import com.spmd.trello.repositories.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class DataExportController {

    @Autowired
    private final ActionRepository _actionRepository;

    DataExportController(ActionRepository actionRepository) {
        this._actionRepository = actionRepository;
    }


}
