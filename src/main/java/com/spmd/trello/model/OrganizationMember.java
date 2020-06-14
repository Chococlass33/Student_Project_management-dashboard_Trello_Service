package com.spmd.trello.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class OrganizationMember
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String id;
    @ManyToOne
    @JoinColumn(name = "idOrganisation",nullable = false)
    private Organization organisation;
    @ManyToOne
    @JoinColumn(name = "idMember",nullable = false)
    private Member member;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;

    public OrganizationMember(String id, Timestamp dateCreated, Timestamp dateLastModified)
    {

        this.id = id;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
    }

    protected OrganizationMember(){}


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Timestamp getDateCreated()
    {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated)
    {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateLastModified()
    {
        return dateLastModified;
    }

    public void setDateLastModified(Timestamp dateLastModified)
    {
        this.dateLastModified = dateLastModified;
    }

    public Member getMember()
    {
        return member;
    }

    public void setMember(Member member)
    {
        this.member = member;
    }

    public Organization getOrganisation()
    {
        return organisation;
    }

    public void setOrganisation(Organization organisation)
    {
        this.organisation = organisation;
    }
}
