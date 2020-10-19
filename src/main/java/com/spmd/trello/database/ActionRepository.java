package com.spmd.trello.database;

import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;

public interface ActionRepository extends CrudRepository<Action, String> {
    List<Action> findAllByBoard(String board);

    Iterable<Action> findAllByBoardAndDateCreated(String board, Date dateCreated);
}
