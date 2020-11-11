package com.spmd.trello.webhooks;

import com.spmd.trello.BadConfig;
import com.spmd.trello.database.Action;
import com.spmd.trello.database.ActionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.sql.Timestamp;
import java.util.Set;

/**
 * Controller to handle requests related to the trello webhook.
 * This includes the check that the webhook exists, as well as the subsequent action information
 */
@Controller // This means that this class is a Controller
@RequestMapping(path = "/webhook") // This means URL's start with /webhook (after Application path)
public class WebhookController {
    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);
    @Autowired
    private ActionRepository actionRepository;

    /**
     * Handles the initial HEAD request from trello to check if the webhook is active
     * A HEAD request can also be handled by GET, making things simpler.
     * <p>
     * Just returns a 200 response code.
     */
    @GetMapping
    public @ResponseBody
    String checkCallback() {
        logger.info("Got Trello Request");
        return "";
    }

    /**
     * Handles a request to make a new webhook for the given model.
     * Attempts to make a new webhook
     */
    @PostMapping(path = "/new")
    public @ResponseBody
    String newWebhook(@RequestBody NewWebhook body) {
        TrelloWebhook webhook = new TrelloWebhook();
        webhook.callbackURL = "https://trello-backend-1.herokuapp.com/webhook";
        webhook.idModel = body.idModel;
        webhook.description = "Autogenerated webhook for SPMD";

        logger.info("Making webhook for '" + webhook.callbackURL + "'");
        URI url = UriComponentsBuilder.fromHttpUrl("https://api.trello.com/1/webhooks")
                .queryParam("key", BadConfig.API_KEY)
                .queryParam("token", body.token)
                .build().toUri();

        RestTemplate restTemplate = new RestTemplate();
        try {
            WebhookResponse response = restTemplate.postForObject(url, webhook, WebhookResponse.class);
            logger.info("Made Webhook");
        } catch (HttpClientErrorException e) {
            logger.error("Unable to make webhook");
            logger.error(e.getMessage());
            String message = e.getMessage();
            if (message != null && message.contains("A webhook with that callback, model, and token already exists")) {
                return "Board already tracked";
            } else {
                return "Unknown Error";
            }
        }

        return "Webhook created";
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
            Action dbAction = jsonToDatabase(body);
            actionRepository.save(dbAction);
        } else {
            logger.info("Ignoring");
        }
        return "";
    }

    /**
     * The list of actions we want to filter for
     */
    private static final Set<String> VALID_ACTIONS = Set.of("createCard",
            "updateCard", "deleteCard", "updateList", "createList", "copyCard");

    /**
     * Checks if the action type is one that we actually care about
     *
     * @param actionType The type of the action
     * @return True if we care, false otherwise
     */
    private boolean isValidAction(String actionType) {
        return VALID_ACTIONS.contains(actionType);
    }

    private Action jsonToDatabase(WebhookAction json) {
        return new Action(json.action.id,
                json.model.getIdBoard(),
                json.action.idMemberCreator,
                json.action.type,
                json.action.getData(), //TODO improve this
                Timestamp.from(json.action.date.toInstant()));
    }
}