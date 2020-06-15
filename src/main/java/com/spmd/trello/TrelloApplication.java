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

}
