package com.spmd.trello.controller;
import com.spmd.trello.BadConfig;
import com.spmd.trello.model.Action;
import com.spmd.trello.repositories.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
public class DataExportController {

    @Autowired
    private final ActionRepository _actionRepository;

    DataExportController(ActionRepository actionRepository) {
        this._actionRepository = actionRepository;
    }

    @GetMapping("/data/export/{boardId}")
    ResponseEntity<TrelloDataExport> exportData(@PathVariable String boardId, @RequestParam String token) {

        // Grab all actions stored corresponding to the board id.
        List<Action> actions = _actionRepository.findAllByBoard(boardId);

        // Grab all members of a board.
        URI url = UriComponentsBuilder.fromHttpUrl("https://api.trello.com/1/boards/" + boardId + "/members")
                .queryParam("key", BadConfig.API_KEY)
                .queryParam("token", token)
                .build().toUri();
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<List<BoardMemberResponse>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<BoardMemberResponse>>(){});
        List<BoardMemberResponse> listOfMembersOfABoard = responseEntity.getBody();

        return ResponseEntity.ok(new TrelloDataExport("","","","",""));
    }

    /**
     * Represents the structure of the Trello data/action to export.
     */
    private static class TrelloDataExport {

        public TrelloDataExport(String memberId, String fullName, String email, String actionType, String actionData) {
            this.memberId = memberId;
            this.fullName = fullName;
            this.email = email;
            this.actionType = actionType;
            this.actionData = actionData;
        }

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
