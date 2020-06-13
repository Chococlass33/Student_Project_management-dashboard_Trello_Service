package com.spmd.trello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // This means that this class is a Controller
@RequestMapping(path = "/webhook") // This means URL's start with /demo (after Application path)
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @GetMapping
    public @ResponseBody
    String newWebhook() {
        return "";
    }

    @PostMapping
    public @ResponseBody
    String receiveAction() {
        logger.info("Action ping!");
        return "";
    }

}