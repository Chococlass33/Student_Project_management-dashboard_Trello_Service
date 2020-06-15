package com.spmd.trello.repositories;

import com.spmd.trello.model.Member;
import com.spmd.trello.model.Organization;
import org.springframework.data.repository.CrudRepository;

public interface OrganizationRepository extends CrudRepository<Organization, String> {
}
