package com.spmd.trello;

import com.spmd.trello.model.Action;
import com.spmd.trello.model.Member;
import com.spmd.trello.repositories.ActionRepository;
import com.spmd.trello.repositories.MemberRepository;
import com.spmd.trello.trelloModel.WebhookAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;

/**
 * Controller to handle requests related to the trello webhook.
 * This includes the check that the webhook exists, as well as the subsequent action information
 */
@Controller // This means that this class is a Controller
@RequestMapping(path = "/webhook") // This means URL's start with /demo (after Application path)
public class WebhookController {
    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);
    @Autowired
    private MemberRepository memberRepository;
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
            Action dbAction = jsonToDatabase(body);
            actionRepository.save(dbAction);
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

    private Action jsonToDatabase(WebhookAction json) {
        Member member = findMember(json.action.idMemberCreator);
        return new Action(json.action.id,
                member,
                json.action.type,
                json.action.data.toString(), //TODO improve this
                Timestamp.from(json.action.date.toInstant()),
                Timestamp.from(Instant.now()));
    }

    /**
     * Obtains the member object for the given id.
     * If the member could not be found, creates a new one
     *
     * @param memberId The ID of the member to use
     * @return The member object
     */
    private Member findMember(String memberId) {
        return memberRepository.findById(memberId)
                .orElse(makeMember(memberId));
    }

    /**
     * Make a new Member with the given ID and store them in the database
     * <p>
     * TODO: Make this properly make a new member, rather than just a dummy one
     *
     * @param memberId The id of the member to make
     * @return The new member
     */
    private Member makeMember(String memberId) {
        Member member = new Member(memberId,
                "someType",
                "Some Name",
                "Some Email",
                Timestamp.from(Instant.now()),
                Timestamp.from(Instant.now()));
        memberRepository.save(member);
        return member;
    }
}