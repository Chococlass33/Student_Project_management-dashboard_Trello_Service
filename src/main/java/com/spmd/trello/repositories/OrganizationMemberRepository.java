package com.spmd.trello.repositories;

import com.spmd.trello.model.Organization;
import com.spmd.trello.model.OrganizationMember;
import org.springframework.data.repository.CrudRepository;

public interface OrganizationMemberRepository extends CrudRepository<OrganizationMember, String> {
}
