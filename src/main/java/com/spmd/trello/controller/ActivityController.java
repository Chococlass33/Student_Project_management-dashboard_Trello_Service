package com.spmd.trello.controller;

import com.spmd.trello.database.Action;
import com.spmd.trello.database.ActionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ActivityController {
    private static final Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @Autowired
    private final ActionRepository actionRepository;

    ActivityController(ActionRepository repository) {
        this.actionRepository = repository;
    }


    @GetMapping("/activity")
    List<Lastmodified> boardHistory(@RequestParam String emails, @RequestParam String boardIds) {

        String[] emailList = emails.split(",", 0);
        return Arrays.stream(emailList).map(email -> new Lastmodified(email, new Timestamp(0).toInstant().toString())).collect(Collectors.toList());
    }

    @GetMapping("/activity/WIP")
    ResponseEntity<List<Lastmodified>> boardHistoryWIP(@RequestParam String emails, @RequestParam String boardIds) {

        //Create the list of emails and board ids.
        String[] emailList = emails.split(",", 0);
        String[] boardList = boardIds.split(",", 0);


        //Create a mapping for each email given
        HashMap<String, Timestamp> emailMap = new HashMap<>();
        for (String email : emailList) {
            emailMap.put(email, new Timestamp(0));
        }

        //Compile the list of actions from the boards given
        List<Action> actions = new ArrayList<Action>();
        for (String boardId : boardList) {
            actions.addAll(actionRepository.findAllByBoard(boardId));
        }
//
//        //For each action, try to see if it's member's email is present in the above map, if so then get the date and compare the latest one
//        for (Action action : actions) {
//            //Check for member in database
//            Optional<Member> member = memberRepository.findById(action.getMember());
//            if (member.isPresent()) {
//                //Check for this member's email in emailmap
//                if (emailMap.get(member.get().getEmail()) != null) {
//                    //Compare current time
//                    if (action.getDateCreated().getTime() > emailMap.get(member.get().getEmail()).getTime()) {
//                        //If the action is sooner, then replace the map.
//                        emailMap.put(member.get().getEmail(), action.getDateCreated());
//                    }
//                }
//            }
//        }

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


}

