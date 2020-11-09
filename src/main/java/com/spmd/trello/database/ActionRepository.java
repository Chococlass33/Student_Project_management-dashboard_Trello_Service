package com.spmd.trello.database;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ActionRepository extends CrudRepository<Action, String> {
    List<Action> findAllByBoard(String board);

    @Transactional
    long deleteAllByBoard(String board);

}
