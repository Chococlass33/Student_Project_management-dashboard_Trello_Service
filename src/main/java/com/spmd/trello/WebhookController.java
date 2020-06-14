package com.spmd.trello;

import com.spmd.trello.trelloModel.WebhookAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

@Controller // This means that this class is a Controller
@RequestMapping(path = "/webhook") // This means URL's start with /demo (after Application path)
public class WebhookController {
    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    @GetMapping
    public @ResponseBody
    String newWebhook() {
        return "";
    }

    @PostMapping
    public @ResponseBody
    String receiveAction(@RequestBody WebhookAction body) {
        if (isValidAction(body.action.type)) {
            logger.info("Relevent Info");
            logger.info(body.toString());
        } else {
            logger.info("Ignoring");
            logger.info(body.toString());
        }

        return "";
    }

    private static final Set<String> VALID_ACTIONS = Set.of("createCard");

    /**
     * Checks if the action type is one that we actually care about
     *
     * @param actionType The type of the action
     * @return True if we care, false otherwise
     */
    private boolean isValidAction(String actionType) {
        return VALID_ACTIONS.contains(actionType);
    }

}