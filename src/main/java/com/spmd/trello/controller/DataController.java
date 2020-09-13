package com.spmd.trello.controller;

import com.spmd.trello.model.Board;
import com.spmd.trello.model.CardMember;
import com.spmd.trello.repositories.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


//TBD, currently stock placeholder from springboot tutorial until i get the entities right
@RestController
class DataController {
    @Autowired
    private final BoardRepository boardRepo;

    DataController(BoardRepository boardRepo) {
        this.boardRepo = boardRepo;
    }

    @GetMapping(path = "/data/cardMembers/{boardId}")
    Map<String, Integer> getAllMembers(@PathVariable String boardId) {
        Board board = boardRepo.findById(boardId).orElseThrow();
        Set<CardMember> cardMembers = board.getCards()
                .stream()
                .flatMap(card -> card.getCardMembers().stream())
                .collect(Collectors.toSet());
        Map<String, Integer> cardCount = new HashMap<>();
        for (CardMember cardMember : cardMembers) {
             String id = cardMember.getMember().getId();
             cardCount.put(id, cardCount.getOrDefault(id, 0) + 1);
        }
        return cardCount;
    }
}