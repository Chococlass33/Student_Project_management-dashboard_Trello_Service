package com.spmd.trello.repositories;

import com.spmd.trello.model.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MemberRepository extends CrudRepository<Member, String> {
    Optional<Member> findById(String id);
}
