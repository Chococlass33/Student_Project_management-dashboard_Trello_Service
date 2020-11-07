package com.spmd.trello.database;

import com.spmd.trello.database.Action;
import com.spmd.trello.database.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MemberRepository extends CrudRepository<Member, String> {
}
