package com.spmd.trello.repositories;

import com.spmd.trello.model.Board;
import com.spmd.trello.model.Card;
import org.springframework.data.repository.CrudRepository;

public interface CardRepository extends CrudRepository<Card, String> {
}
