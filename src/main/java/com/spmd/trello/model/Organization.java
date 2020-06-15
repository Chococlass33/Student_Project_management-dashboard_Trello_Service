package com.spmd.trello.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Organization
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String id;
    private String name;
    private String displayName;
    private String description;
    private String descData;
    private String teamType;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;

    @OneToMany(
            mappedBy = "organisation",
            cascade=CascadeType.ALL
    )
    private Set<OrganizationMember> organisationMembers;

    @OneToMany(
            mappedBy = "organisation",
            cascade=CascadeType.ALL
    )
    private Set<Board> boards;

    public Organization(String id, String name, String displayName, String description, String descData, String teamType, Timestamp dateCreated, Timestamp dateLastModified)
    {

        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.descData = descData;
        this.teamType = teamType;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
        organisationMembers = new HashSet<OrganizationMember>();
        boards = new HashSet<Board>();
    }
    protected Organization(){}


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescData()
    {
        return descData;
    }

    public void setDescData(String descData)
    {
        this.descData = descData;
    }

    public String getTeamType()
    {
        return teamType;
    }

    public void setTeamType(String teamType)
    {
        this.teamType = teamType;
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

    public Set<OrganizationMember> getOrganisationMember()
    {
        return organisationMembers;
    }

    public void setOrganisationMember(Set<OrganizationMember> organizationMembers)
    {
        this.organisationMembers = organizationMembers;
        for(OrganizationMember organizationMember:organizationMembers)
        {
            organizationMember.setOrganisation(this);
        }
    }

    public Set<Board> getBoard()
    {
        return boards;
    }

    public void setBoard(Set<Board> boards)
    {
        this.boards = boards;
        for(Board board:boards)
        {
            board.setOrganisation(this);
        }
    }
}
