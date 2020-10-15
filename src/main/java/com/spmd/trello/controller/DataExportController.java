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

    /**
     * Represents the structure of the Trello data/action to export.
     */
    private static class TrelloDataExport {

        // User information.
        public String memberId;
        public String fullName;
        public String email;

        // Action information.
        public String actionType;
        public String actionData; // This data should be different based on the action.
    }

    /**
     * Represents a subset of the structure of the response from getting all members of a board from the Trello API.
     * This will only contain an ID as a means of subsequently and individually accessing a member's details.
     */
    private static class BoardMemberResponse {
        public String memberId;
    }

    /**
     * Represents a subset of the structure of the response from getting a particular member from the Trello API.
     */
    private static class MemberResponse {
        public String memberId;
        public String fullName;
        public String email;
    }
}
