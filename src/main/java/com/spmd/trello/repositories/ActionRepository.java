package com.spmd.trello.repositories;

import com.spmd.trello.model.Action;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;

public interface ActionRepository extends CrudRepository<Action, String> {
    Iterable<Action> findAllByBoard(String board);
    Iterable<Action> findAllByBoardAndDateCreated(String board, Date dateCreated);
}
