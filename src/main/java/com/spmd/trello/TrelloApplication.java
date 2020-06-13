package com.spmd.trello;

import com.spmd.trello.trelloModel.WebhookPost;
import com.spmd.trello.trelloModel.WebhookResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@SpringBootApplication
public class TrelloApplication {
    private static final Logger logger = LoggerFactory.getLogger(TrelloApplication.class);


    public static void main(String[] args) {
        SpringApplication.run(TrelloApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.defaultHeader("Accept", "application/json").build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) {
        return args -> {
            WebhookPost webhook = new WebhookPost();
            webhook.callbackURL = "http://0c26b55f33fe.ngrok.io/webhook";
            webhook.idModel = "58d89302cd53f765b45d1f3c";
            webhook.description = "Test webhook Spring";


            URI url = UriComponentsBuilder.fromHttpUrl("https://api.trello.com/1/webhooks")
                    .queryParam("key", API_KEY)
                    .queryParam("token", TOKEN)
                    .build().toUri();
            try {
                WebhookResponse response = restTemplate.postForObject(url, webhook, WebhookResponse.class);
                logger.info(response.toString());
            } catch (HttpClientErrorException e) {
                logger.info(e.getMessage());
            }
        };
    }

}
