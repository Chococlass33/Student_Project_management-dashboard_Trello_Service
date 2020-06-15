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

/**
 * Controller to handle requests related to the trello webhook.
 * This includes the check that the webhook exists, as well as the subsequent action information
 */
@Controller // This means that this class is a Controller
@RequestMapping(path = "/webhook") // This means URL's start with /demo (after Application path)
public class WebhookController {
    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    /**
     * Handles the initial HEAD request from trello to check if the webhook is active
     * A HEAD request can also be handled by GET, making things simpler.
     * <p>
     * Just returns a 200 response code.
     */
    @GetMapping
    public @ResponseBody
    String newWebhook() {
        return "";
    }

    /**
     * Handles a new action form trello
     *
     * @param body The information for the action
     */
    @PostMapping
    public @ResponseBody
    String receiveAction(@RequestBody WebhookAction body) {
        if (isValidAction(body.action.type)) {
            logger.info("Relevant Info");
        } else {
            logger.info("Ignoring");
        }
        logger.info(body.toString());

        return "";
    }

    /**
     * The list of actions we want to filter for
     */
    private static final Set<String> VALID_ACTIONS = Set.of("createCard", "updateCard", "deleteCard");

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