package com.spmd.trello;

import com.spmd.trello.model.Action;
import com.spmd.trello.model.Board;
import com.spmd.trello.model.BoardMembership;
import com.spmd.trello.model.Card;
import com.spmd.trello.model.CardLabel;
import com.spmd.trello.model.CardMember;
import com.spmd.trello.model.CheckItem;
import com.spmd.trello.model.Checklist;
import com.spmd.trello.model.Label;
import com.spmd.trello.model.List;
import com.spmd.trello.model.Member;
import com.spmd.trello.repositories.ActionRepository;
import com.spmd.trello.repositories.BoardMembershipRepository;
import com.spmd.trello.repositories.BoardRepository;
import com.spmd.trello.repositories.CardLabelRepository;
import com.spmd.trello.repositories.CardMemberRepository;
import com.spmd.trello.repositories.CardRepository;
import com.spmd.trello.repositories.CheckItemRepository;
import com.spmd.trello.repositories.ChecklistRepository;
import com.spmd.trello.repositories.LabelRepository;
import com.spmd.trello.repositories.ListRepository;
import com.spmd.trello.repositories.MemberRepository;
import com.spmd.trello.trelloModel.NewWebhook;
import com.spmd.trello.trelloModel.TrelloBoard;
import com.spmd.trello.trelloModel.WebhookAction;
import com.spmd.trello.trelloModel.WebhookPost;
import com.spmd.trello.trelloModel.WebhookResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.sql.Timestamp;
import java.time.Instant;
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
    private MemberRepository memberRepository;
    @Autowired
    private ActionRepository actionRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ListRepository listRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private CardLabelRepository cardLabelRepository;
    @Autowired
    private BoardMembershipRepository boardMembershipRepository;
    @Autowired
    private ChecklistRepository checklistRepository;
    @Autowired
    private CheckItemRepository checkItemRepository;
    @Autowired
    private CardMemberRepository cardMemberRepository;


    /**
     * Handles the initial HEAD request from trello to check if the webhook is active
     * A HEAD request can also be handled by GET, making things simpler.
     * <p>
     * Just returns a 200 response code.
     */
    @GetMapping
    public @ResponseBody
    String checkCallback() {
        return "";
    }

    /**
     * Handles a request to make a new webhook for the given model.
     * Attempts to make a new webhook
     */
    @PostMapping(path = "/new")
    public @ResponseBody
    String newWebhook(@RequestBody NewWebhook body) {
        logger.debug("PING");
        WebhookPost webhook = new WebhookPost();
        webhook.callbackURL = "http://167.99.7.70:5002/webhook";
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
            logger.info(response.toString());
        } catch (HttpClientErrorException e) {
            logger.error("Unable to make webhook");
            logger.error(e.getMessage());
            String message = e.getMessage();
            if (message != null && message.contains("A webhook with that callback, model, and token already exists")) {
                /* Webhook already made, lets try to scape data */
                if (!checkBoardExists(body.idModel)) {
                    tryScrapeBoard(body.idModel, body.token);
                    return "Webhook already existed, but board still scraped";
                } else {
                    logger.error("Board data already exists. Skipping");
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Board already being tracked");
                }
            }
        }

        tryScrapeBoard(body.idModel, body.token);

        return "Webhook created & Board Scraped";
    }

    /**
     * Attempts to scape the boards
     *
     * @param boardId The id of the board to scape
     * @param token   The api token to use
     * @throws ResponseStatusException If there was an issue scraping the board
     */
    private void tryScrapeBoard(String boardId, String token) {
        try {
            scrapeBoard(boardId, token);
        } catch (HttpClientErrorException e) {
            logger.error("Unable to scrape board data");
            logger.error(e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Checks if a board exists or not
     *
     * @param id The id of the board
     * @return True if an entry matches, false otherwise
     */
    private boolean checkBoardExists(String id) {
        return boardRepository.existsById(id);
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
                boardRepository.findById(json.model.getIdBoard()).orElseThrow(),
                member,
                json.action.type,
                json.action.getData(), //TODO improve this
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
                "Fake Type",
                "Fake Name",
                "Fake Email",
                Timestamp.from(Instant.now()),
                Timestamp.from(Instant.now()));
        memberRepository.save(member);
        return member;
    }

    /**
     * Scrapes the whole board into the database
     *
     * @param boardId The id of the board to use
     */
    private void scrapeBoard(String boardId, String token) {

        URI url = UriComponentsBuilder.fromHttpUrl("https://api.trello.com/1/boards/")
                .path(boardId)
                .queryParam("key", BadConfig.API_KEY)
                .queryParam("token", token)
                .queryParam("cards", "all")
                .queryParam("labels", "all")
                .queryParam("members", "all")
                .queryParam("checklists", "all")
                .queryParam("lists", "all")
                .build().toUri();

        RestTemplate restTemplate = new RestTemplate();
        TrelloBoard board = restTemplate.getForObject(url, TrelloBoard.class);
        saveBoardToDatabase(board);
    }

    /**
     * Saves the given board to the database.
     * Converts each of the json representations into database representations.
     * Order is important as it ensures that the relations can be constructed correctly
     *
     * @param board The board data to save to the database
     */
    private void saveBoardToDatabase(TrelloBoard board) {
        Board dbBoard = convertBoard(board);
        boardRepository.save(dbBoard);

        board.lists.stream()
                .map(this::convertList)
                .forEach(listRepository::save);

        board.cards.stream()
                .map(this::convertCard)
                .forEach(cardRepository::save);

        board.labels.stream()
                .map(this::convertLabel)
                .forEach(labelRepository::save);

        board.cards.stream()
                .flatMap(card -> card.idLabels
                        .stream()
                        .map(idLabel -> createCardLabel(card.id, idLabel)))
                .forEach(cardLabelRepository::save);

        board.members.stream()
                .map(this::convertMember)
                .forEach(memberRepository::save);

        board.members.stream()
                .map(member -> createBoardMembership(board.id, member.id))
                .forEach(boardMembershipRepository::save);

        board.cards.stream()
                .flatMap(card -> card.idMembers.stream().map(idMember -> createCardMember(card.id, idMember)))
                .forEach(cardMemberRepository::save);

        board.checklists.stream()
                .map(this::convertChecklist)
                .forEach(checklistRepository::save);

        board.checklists.stream()
                .flatMap(checklist -> checklist.checkItems.stream().map(this::convertCheckItem))
                .forEach(checkItemRepository::save);
    }

    private Board convertBoard(TrelloBoard board) {
        return new Board(board.id,
                board.name,
                board.desc,
                board.descData.map(Object::toString).orElse(null),
                board.shortUrl,
                Timestamp.from(Instant.now()),
                Timestamp.from(Instant.now()));
    }

    private List convertList(TrelloBoard.TrelloList list) {
        return new List(list.id,
                boardRepository.findById(list.idBoard).orElseThrow(),
                list.name,
                list.pos,
                Timestamp.from(Instant.now()),
                Timestamp.from(Instant.now()));
    }

    private Card convertCard(TrelloBoard.Card card) {
        return new Card(card.id,
                listRepository.findById(card.idList).orElseThrow(),
                boardRepository.findById(card.idBoard).orElseThrow(),
                card.checkItemStates.map(Object::toString).orElse(null), //TODO: Check this
                card.closed ? 0 : 1,
                Timestamp.from(Instant.parse(card.dateLastActivity)),
                card.desc,
                card.descData.map(Object::toString).orElse(null),
                card.due.map(due -> Timestamp.from(Instant.parse(due))).orElse(null),
                0,
                card.name,
                card.pos,
                card.shortUrl,
                Timestamp.from(Instant.now()),
                Timestamp.from(Instant.now()));
    }

    private Label convertLabel(TrelloBoard.Label label) {
        return new Label(label.id,
                boardRepository.findById(label.idBoard).orElseThrow(),
                label.name,
                label.color,
                Timestamp.from(Instant.now()),
                Timestamp.from(Instant.now()));
    }

    private CardLabel createCardLabel(String cardId, String labelId) {
        return new CardLabel(
                cardRepository.findById(cardId).orElseThrow(),
                labelRepository.findById(labelId).orElseThrow(),
                Timestamp.from(Instant.now()),
                Timestamp.from(Instant.now()));
    }

    private Member convertMember(TrelloBoard.Member member) {
        return new Member(member.id,
                member.memberType,
                member.fullName,
                member.email,
                Timestamp.from(Instant.now()),
                Timestamp.from(Instant.now()));
    }

    private BoardMembership createBoardMembership(String boardId, String memberId) {
        return new BoardMembership(
                boardRepository.findById(boardId).orElseThrow(),
                memberRepository.findById(memberId).orElseThrow(),
                null,
                Timestamp.from(Instant.now()),
                Timestamp.from(Instant.now()));
    }

    private CardMember createCardMember(String cardId, String memberId) {
        return new CardMember(
                cardRepository.findById(cardId).orElseThrow(),
                memberRepository.findById(memberId).orElseThrow(),
                Timestamp.from(Instant.now()),
                Timestamp.from(Instant.now()));

    }

    private Checklist convertChecklist(TrelloBoard.Checklist checklist) {
        return new Checklist(checklist.id,
                cardRepository.findById(checklist.idCard).orElseThrow(),
                checklist.name,
                checklist.pos,
                Timestamp.from(Instant.now()),
                Timestamp.from(Instant.now()));
    }

    private CheckItem convertCheckItem(TrelloBoard.Checklist.CheckItem checkItem) {
        return new CheckItem(checkItem.id,
                checklistRepository.findById(checkItem.idChecklist).orElseThrow(),
                checkItem.name,
                checkItem.nameData.map(Object::toString).orElse(null),
                checkItem.pos,
                checkItem.state,
                Timestamp.from(Instant.now()),
                Timestamp.from(Instant.now()));
    }
}