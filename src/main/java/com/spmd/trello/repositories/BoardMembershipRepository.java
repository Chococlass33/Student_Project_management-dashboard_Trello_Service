package com.spmd.trello.repositories;

import com.spmd.trello.model.Board;
import com.spmd.trello.model.BoardMembership;
import org.springframework.data.repository.CrudRepository;

public interface BoardMembershipRepository extends CrudRepository<BoardMembership, String> {
}
