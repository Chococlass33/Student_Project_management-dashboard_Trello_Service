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

    /**
     * Produces the number of cards each member is assigned to
     */
    @GetMapping(path = "/data/cardMembers/{boardId}")
    ResponseEntity<List<GraphEntry<Long>>> getAllMembers(@PathVariable String boardId, @RequestParam String token) {
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
        Arrays.stream(members) // Let us manipulate
                .forEach(entry -> nameMap.put(entry.member.id, entry.member.fullName)); //Add an entry in the name map

        Map<String, Long> valueMap =
                Arrays.stream(cards)
                        .flatMap(card -> Arrays.stream(card.idMembers)) // Convert each card into all it's members
                        .map(id -> nameMap.getOrDefault(id, "Unknown")) // Convert each member ID into it's name
                        .collect(Collectors.groupingBy(e -> e, Collectors.counting())); // Collect and count the numbers

        List<GraphEntry<Long>> result = valueMap.entrySet() // Get all the entries in the map
                .stream() //Let us manipulate
                .map(entry -> new GraphEntry<>(entry.getKey(), entry.getValue())) // Convert into graph entries
                .collect(Collectors.toList()); //Convert into a list

        /* Return the result */
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Get the number of cards in each list
     */
    @GetMapping(path = "/data/listSizes/{boardId}")
    ResponseEntity<List<GraphEntry<Long>>> getListSize(@PathVariable String boardId, @RequestParam String token) {
        /* Do API calls to get data */
        RestTemplate restTemplate = new RestTemplate();

        URI url = UriComponentsBuilder.fromHttpUrl("https://api.trello.com/1/boards/" + boardId + "/lists")
                .queryParam("key", BadConfig.API_KEY)
                .queryParam("token", token)
                .build().toUri();
        TrelloList[] lists = restTemplate.getForObject(url, TrelloList[].class);

        url = UriComponentsBuilder.fromHttpUrl("https://api.trello.com/1/boards/" + boardId + "/cards")
                .queryParam("key", BadConfig.API_KEY)
                .queryParam("token", token)
                .queryParam("fields", "idList")
                .build().toUri();
        Card[] cards = restTemplate.getForObject(url, Card[].class);

        /* Check we got values fine */
        if (lists == null || cards == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        /* Process data */
        Map<String, String> nameMap = new HashMap<>();
        Arrays.stream(lists) // Let us manipulate
                .forEach(entry -> nameMap.put(entry.id, entry.name)); //Add an entry in the name map

        Map<String, Long> valueMap =
                Arrays.stream(cards) //Let us manipulate
                        .map(card -> nameMap.getOrDefault(card.idList, "Unknown")) // Convert card to their list's name
                        .collect(Collectors.groupingBy(e -> e, Collectors.counting())); // Collect and count the numbers

        List<GraphEntry<Long>> result = valueMap.entrySet() // Get all the entries in the map
                .stream() //Let us manipulate
                .map(entry -> new GraphEntry<>(entry.getKey(), entry.getValue())) // Convert into graph entries
                .collect(Collectors.toList()); //Convert into a list

        /* Return the result */
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * An entry in a graph
     *
     * @param <T> The type of the value for the data
     */
    private static class GraphEntry<T> {
        public String label;
        public T value;

        private GraphEntry(String label, T value) {
            this.label = label;
            this.value = value;
        }
    }

    /**
     * The card data returned from trello
     */
    private static class Card {
        public String id;
        public String idList;
        public String[] idMembers;
    }

    /**
     * The list data returned from trello
     */
    private static class TrelloList {
        public String id;
        public String name;
    }

    /**
     * The member data returned form trello
     */
    private static class Member {
        public MemberDetails member;

        private static class MemberDetails {
            public String fullName;
            public String id;
        }
    }

}
