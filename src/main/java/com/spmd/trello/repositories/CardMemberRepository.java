package com.spmd.trello.repositories;

import com.spmd.trello.model.CardMember;
import org.springframework.data.repository.CrudRepository;

public interface CardMemberRepository extends CrudRepository<CardMember, Integer> {
}
