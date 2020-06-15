package com.spmd.trello.repositories;

import com.spmd.trello.model.Checklist;
import com.spmd.trello.model.List;
import org.springframework.data.repository.CrudRepository;

public interface ChecklistRepository extends CrudRepository<Checklist, String> {
}
