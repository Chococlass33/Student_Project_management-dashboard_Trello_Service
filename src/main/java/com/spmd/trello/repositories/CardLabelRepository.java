package com.spmd.trello.repositories;

import com.spmd.trello.model.Card;
import com.spmd.trello.model.CardLabel;
import org.springframework.data.repository.CrudRepository;

public interface CardLabelRepository extends CrudRepository<CardLabel, Integer> {
}
