package com.spmd.trello.repositories;

import com.spmd.trello.model.CheckItem;
import org.springframework.data.repository.CrudRepository;

public interface CheckItemRepository extends CrudRepository<CheckItem, String> {
}
