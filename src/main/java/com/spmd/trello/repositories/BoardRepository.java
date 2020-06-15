package com.spmd.trello.repositories;

import com.spmd.trello.model.Action;
import com.spmd.trello.model.Board;
import org.springframework.data.repository.CrudRepository;

public interface BoardRepository extends CrudRepository<Board, String> {
}
