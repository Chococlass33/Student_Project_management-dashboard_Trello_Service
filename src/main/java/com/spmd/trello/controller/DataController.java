package com.spmd.trello.controller;

import com.spmd.trello.model.Board;
import com.spmd.trello.model.BoardMembership;
import com.spmd.trello.model.CardMember;
import com.spmd.trello.model.List;
import com.spmd.trello.model.Member;
import com.spmd.trello.repositories.BoardRepository;
import com.spmd.trello.trelloModel.WebhookAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
class DataController {
    private static final Logger logger = LoggerFactory.getLogger(DataController.class);
    @Autowired
    private final BoardRepository boardRepo;

    DataController(BoardRepository boardRepo) {
        this.boardRepo = boardRepo;
    }

    @GetMapping(path = "/data/cardMembers/{boardId}")
    Map<String, Integer> getAllMembers(@PathVariable String boardId) {
        logger.info("Getting card memberships for board " + boardId);
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

    @GetMapping(path = "/data/boardMembers/{boardId}")
    Iterable<Member> getBoardMember(@PathVariable String boardId) {
        return boardRepo.findById(boardId).stream().flatMap(board -> board.getBoardMemberships().stream())
                .map(BoardMembership::getMember).collect(Collectors.toList());
    }

    @GetMapping(path = "/data/listSizes/{boardId}")
    Map<String, Integer> getListSize(@PathVariable String boardId) {
        Map<String, Integer> cardCount = new HashMap<>();
        boardRepo.findById(boardId).stream() // Get the board
                .flatMap(board -> board.getLists().stream()) // Get all the lists
                .forEach(list -> cardCount.put(list.getId(), list.getCards().size())); // Insert the size of this list
        return cardCount;
    }

    @GetMapping(path = "/data/boardLists/{boardId}")
    Iterable<List> getBoardLists(@PathVariable String boardId) {
        return boardRepo.findById(boardId).stream() // Get the board
                .flatMap(board -> board.getLists().stream())
                .collect(Collectors.toList());
    }
}