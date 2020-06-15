package com.spmd.trello.repositories;

import com.spmd.trello.model.Label;
import com.spmd.trello.model.List;
import org.springframework.data.repository.CrudRepository;

public interface LabelRepository extends CrudRepository<Label, String> {
}
