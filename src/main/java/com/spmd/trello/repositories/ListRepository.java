package com.spmd.trello.repositories;

import com.spmd.trello.model.Card;
import com.spmd.trello.model.List;
import org.springframework.data.repository.CrudRepository;

public interface ListRepository extends CrudRepository<List, String> {
}
