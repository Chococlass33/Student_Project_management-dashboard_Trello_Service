package com.spmd.trello.repositories;

import com.spmd.trello.model.Action;
import com.spmd.trello.model.Member;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, String> {
}
