package com.spmd.trello.controller;
import com.spmd.trello.BadConfig;
import com.spmd.trello.model.Action;
import com.spmd.trello.repositories.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The DataExportController class will act as an endpoint that deals with generating and manipulating data to be
 * exported for CSV usage.
 */
@RestController
public class DataExportController {

    @Autowired
    private final ActionRepository _actionRepository;

    /**
     * Represents the constructor of the class.
     * @param actionRepository - Injects the action repository for use.
     */
    public DataExportController(ActionRepository actionRepository) {
        this._actionRepository = actionRepository;
    }

    /**
     * The exportData method takes a board ID and token, and will return a CSV-friendly response of actions and members.
     * @param boardId - ID of the board to extract data from
     * @param token - Token used to verify request to the Trello API
     * @return - Will return a CSV-friendly response of actions and members.
     */
    @GetMapping("/data/export/{boardId}")
    ResponseEntity<List<TrelloDataExport>> exportData(@PathVariable String boardId, @RequestParam String token) {

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

        // If there are no members in a board found, return response entity with Http status NOT FOUND.
        if (listOfMembersOfABoard == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Grab each member's detailed information individually and store in a HashMap.
        HashMap<String, MemberResponse> mapOfMembers = new HashMap<>();
        listOfMembersOfABoard.stream()
                .filter(member -> member != null) // Check if the particular member is null, and if so, filter it out.
                .forEach(member -> {
                    ResponseEntity<MemberResponse> response = GetMember(member.memberId, token);

                    if (response != null) {
                        mapOfMembers.put(response.getBody().memberId, response.getBody());
                    }
                });

        // With each action, we can get the corresponding member and create a TrelloDataExport object.
        List<TrelloDataExport> exportData = actions.stream()
                .filter(action -> action != null) // Check if the particular action is null, and if so, filter it out.
                .map(action -> {
                    MemberResponse memberResponse = mapOfMembers.get(action.getMember());
                    return new TrelloDataExport(memberResponse.memberId,
                                                memberResponse.fullName,
                                                memberResponse.email,
                                                action.getType(),
                                                action.getData(),
                                                action.getDateCreated());
                }).collect(Collectors.toList());

        // Update data in the action to be human-readable.
        exportData = clarifyActionData(exportData);

        return new ResponseEntity<List<TrelloDataExport>>(exportData, HttpStatus.FOUND);
    }

    /**
     * Helper method that traverses a list of TrelloDataExport objects and alters the actionData field to be readable.
     * @param exportData - data object to manipulate
     * @return
     */
    private List<TrelloDataExport> clarifyActionData(List<TrelloDataExport> exportData) {
        exportData.stream()
                .forEach(data -> {
                    data.actionData = determineAction(data);
                });

        return exportData;
    }

    /**
     * Helper method to convert actionData into readable form.
     * @param data - data object to draw insights from.
     * @return - Will return a string that details the action.
     */
    private String determineAction(TrelloDataExport data) {
        switch(data.actionType) {
            default:
                break;
        }

        return "";
    }

    /**
     * Grabs a member from the Trello API, and returns a ResponseEntity that contains information on the member.
     * @param memberId - ID of the member to retrieve.
     * @param token - Token used to verify request to the Trello API
     * @return - Will return information on the member.
     */
    private ResponseEntity<MemberResponse> GetMember(String memberId, String token) {

        URI url = UriComponentsBuilder.fromHttpUrl("https://api.trello.com/1/members/" + memberId)
                .queryParam("key", BadConfig.API_KEY)
                .queryParam("token", token)
                .build().toUri();
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<MemberResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<MemberResponse>(){});

        // If there is no information found on the particular member, return a response entity with Http status NOT FOUND.
        if (responseEntity == null) {
            return null;
        }

        return responseEntity;
    }

    /**
     * Represents the structure of the Trello data/action to export.
     */
    private static class TrelloDataExport {

        public TrelloDataExport(String memberId, String fullName, String email, String actionType, String actionData, Timestamp dateCreated) {
            this.memberId = memberId;
            this.fullName = fullName;
            this.email = email;
            this.actionType = actionType;
            this.actionData = actionData;
            this.dateCreated = dateCreated;
        }

        // User information.
        public String memberId;
        public String fullName;
        public String email;

        // Action information.
        public String actionType;
        public String actionData; // This data should be different based on the action.
        public Timestamp dateCreated; // The timestamp that the action was performed.
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
