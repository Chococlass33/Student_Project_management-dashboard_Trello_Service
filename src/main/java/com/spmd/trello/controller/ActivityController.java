package com.spmd.trello.controller;

import com.spmd.trello.BadConfig;
import com.spmd.trello.database.Action;
import com.spmd.trello.database.ActionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RestController
public class ActivityController {
    private static final Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @Autowired
    private final ActionRepository actionRepository;

    ActivityController(ActionRepository repository) {
        this.actionRepository = repository;
    }

    @GetMapping("/activity")
    ResponseEntity<List<Lastmodified>> boardHistory(@RequestParam String emails, @RequestParam String boardIds, @RequestParam String token) {

        //Create the list of emails and board ids.
        String[] emailList = emails.split(",", 0);
        String[] boardList = boardIds.split(",", 0);

        List<BoardMemberResponse> listOfMembersOfABoard = new ArrayList<>();
        for (String s : boardList) {
            URI url = UriComponentsBuilder.fromHttpUrl("https://api.trello.com/1/boards/" + s + "/members")
                    .queryParam("key", BadConfig.API_KEY)
                    .queryParam("token", token)
                    .build().toUri();
            RestTemplate restTemplate = new RestTemplate();

            BoardMemberResponse[] response = restTemplate.getForObject(url, BoardMemberResponse[].class);
            if (response != null) {
                listOfMembersOfABoard.addAll(Arrays.asList(response));
            }
        }

        HashMap<String, MemberResponse> mapOfMembers = new HashMap<>();
        listOfMembersOfABoard.stream()
                .filter(Objects::nonNull) // Check if the particular member is null, and if so, filter it out.
                .forEach(member -> {
                    MemberResponse response = GetMember(member.id, token);
                    if (response != null) {
                        mapOfMembers.put(response.id, response);
                    }
                });


        //Create a mapping for each email given
        HashMap<String, Timestamp> emailMap = new HashMap<>();
        for (String email : emailList) {
            emailMap.put(email, new Timestamp(0));
        }

        //Compile the list of actions from the boards given
        List<Action> actions = new ArrayList<>();
        for (String boardId : boardList) {
            actions.addAll(actionRepository.findAllByBoard(boardId));
        }

        //For each action, try to see if it's member's email is present in the above map, if so then get the date and compare the latest one
        for (Action action : actions) {
            //Check for member in database
            MemberResponse memberResponse = mapOfMembers.get(action.getMember());
            if (memberResponse != null) {
                //Check for this member's email in emailmap
                if (emailMap.get(memberResponse.email) != null) {
                    //Compare current time
                    if (action.getDateCreated().getTime() > emailMap.get(memberResponse.email).getTime()) {
                        //If the action is sooner, then replace the map.
                        emailMap.put(memberResponse.email, action.getDateCreated());
                    }
                }
            }
        }

        //List to store response in
        List<Lastmodified> response = new ArrayList<>();

        //Populate that list for each email in email list
        for (String email : emailList) {
            //Create a date string
            String date = emailMap.get(email).toInstant().toString();
            //If the date found is still 0, set to null
            if (emailMap.get(email).getTime() == 0) {
                date = null;
            }
            //Create a Lastmodified, then put into the response
            Lastmodified temp = new Lastmodified(email, date);
            response.add(temp);
        }

        return ResponseEntity.ok(response);

    }

    private static class Lastmodified {
        public String email;
        public String date;

        public Lastmodified(String email, String date) {
            this.email = email;
            this.date = date;
        }

    }

    /**
     * Represents a subset of the structure of the response from getting all members of a board from the Trello API.
     * This will only contain an ID as a means of subsequently and individually accessing a member's details.
     */
    private static class BoardMemberResponse {
        public String id;
    }

    /**
     * Represents a subset of the structure of the response from getting a particular member from the Trello API.
     */
    private static class MemberResponse {
        public String id;
        public String fullName;
        public String email;
    }

    private MemberResponse GetMember(String memberId, String token) {
        URI url = UriComponentsBuilder.fromHttpUrl("https://api.trello.com/1/members/" + memberId)
                .queryParam("key", BadConfig.API_KEY)
                .queryParam("token", token)
                .build().toUri();
        RestTemplate restTemplate = new RestTemplate();

        // If there is no information found on the particular member, return a response entity with Http status NOT FOUND.

        return restTemplate.getForObject(url, MemberResponse.class);
    }


}

