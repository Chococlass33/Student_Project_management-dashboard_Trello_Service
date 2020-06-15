package com.spmd.trello;

import com.spmd.trello.model.Member;
import com.spmd.trello.repositories.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.time.Instant;

@Controller // This means that this class is a Controller
@RequestMapping(path = "/webhook") // This means URL's start with /demo (after Application path)
public class MainController {
    @Autowired
    private MemberRepository memberRepository;

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @GetMapping
    public @ResponseBody
    String newWebhook() {
        return "";
    }

    @PostMapping
    public @ResponseBody
    String receiveAction() {
        logger.info("Ping. Trying to insert dummy data");

        Member member = new Member("a", "memType", "Full Name", "email@domain.com", Timestamp.from(Instant.now()), Timestamp.from(Instant.now()));
        memberRepository.save(member);

        return "";
    }

}