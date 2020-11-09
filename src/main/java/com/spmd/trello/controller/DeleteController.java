package com.spmd.trello.controller;

import com.spmd.trello.BadConfig;
import com.spmd.trello.database.ActionRepository;
import com.spmd.trello.webhooks.TrelloWebhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;

@RestController
@Transactional
public class DeleteController {
    private static final Logger logger = LoggerFactory.getLogger(DeleteController.class);

    @Autowired
    private final ActionRepository actionRepository;

    DeleteController(ActionRepository repository) {
        this.actionRepository = repository;
    }

    @Transactional
    @DeleteMapping("/delete/{boardId}")
    ResponseEntity<Void> deleteActions(@PathVariable String boardId, @RequestParam String token) {
        URI url = UriComponentsBuilder.fromHttpUrl("https://api.trello.com/1/tokens/" + token + "/webhooks")
                .queryParam("key", BadConfig.API_KEY)
                .build().toUri();

        RestTemplate restTemplate = new RestTemplate();

        TrelloWebhook[] response = restTemplate.getForObject(url, TrelloWebhook[].class);
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Arrays.stream(response)
                .filter(trelloWebhook -> trelloWebhook.idModel.equals(boardId))
                .forEach(trelloWebhook -> deleteWebhook(token, trelloWebhook));
        logger.info("Deleted " + response.length + " webhook(s)");

        long deletedCount = actionRepository.deleteAllByBoard(boardId);
        logger.info("Deleted " + deletedCount + " actions(s)");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void deleteWebhook(String token, TrelloWebhook webhook) {
        URI url = UriComponentsBuilder.fromHttpUrl("https://api.trello.com/1/webhooks/" + webhook.id)
                .queryParam("key", BadConfig.API_KEY)
                .queryParam("token", token)
                .build().toUri();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(url);
    }

}
