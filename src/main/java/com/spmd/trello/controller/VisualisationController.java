package com.spmd.trello.controller;

import com.spmd.trello.BadConfig;
import com.spmd.trello.repositories.BoardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class VisualisationController {
    private static final Logger logger = LoggerFactory.getLogger(VisualisationController.class);
    @Autowired
    private final BoardRepository boardRepo;

    VisualisationController(BoardRepository boardRepo) {
        this.boardRepo = boardRepo;
    }


    /**
     * Produces the number of cards each member is assigned to
     */
    @GetMapping(path = "/data/cardMembers/{boardId}")
    ResponseEntity<List<GraphEntry<Integer>>> getAllMembers(@PathVariable String boardId, @RequestParam String token) {
        /* Do API calls to get data */
        RestTemplate restTemplate = new RestTemplate();

        URI url = UriComponentsBuilder.fromHttpUrl("https://api.trello.com/1/boards/" + boardId + "/cards")
                .queryParam("key", BadConfig.API_KEY)
                .queryParam("token", token)
                .build().toUri();
        Card[] cards = restTemplate.getForObject(url, Card[].class);

        url = UriComponentsBuilder.fromHttpUrl("https://api.trello.com/1/boards/" + boardId + "/memberships")
                .queryParam("key", BadConfig.API_KEY)
                .queryParam("token", token)
                .queryParam("member", true)
                .queryParam("member_fields", "id,fullName")
                .build().toUri();
        Member[] members = restTemplate.getForObject(url, Member[].class);

        /* Check we got values fine */
        if (members == null || cards == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        /* Process data */
        Map<String, String> nameMap = new HashMap<>();
        Arrays.stream(members) // Le tus manipulate
                .forEach(entry -> nameMap.put(entry.member.id, entry.member.fullName)); //Add an entry in the name map

        Map<String, Integer> valueMap = new HashMap<>();
        Arrays.stream(cards)
                .forEach(card -> Arrays.stream(card.idMembers)
                        .map(id -> nameMap.getOrDefault(id, "Unknown")) // Convert id to name
                        .forEach(name -> valueMap.put(name, valueMap.getOrDefault(name, 0) + 1))); // Increment the entry for that name

        List<GraphEntry<Integer>> result = valueMap.entrySet() // Get all the entries in the map
                .stream() //Let us manipulate
                .map(entry -> new GraphEntry<>(entry.getKey(), entry.getValue())) // Convert into graph entries
                .collect(Collectors.toList()); //Convert into a list

        /* Return the result */
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private static class GraphEntry<T> {
        public String label;
        public T value;

        private GraphEntry(String label, T value) {
            this.label = label;
            this.value = value;
        }
    }

    private static class Card {
        public String[] idMembers;
    }

    private static class Member {
        public MemberDetails member;

        private static class MemberDetails {
            public String fullName;
            public String id;
        }
    }

}
