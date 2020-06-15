package com.spmd.trello.model;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Member
{
    @Id
//    @GeneratedValue(generator="system-uuid")
//    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;
    private String memberType;
    private String fullName;
    private String email;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;


    @OneToMany(
            mappedBy = "member",
            cascade=CascadeType.ALL
    )
    private Set<Action> actions;

    @OneToMany(
            mappedBy = "member",
            cascade=CascadeType.ALL
    )
    private Set<OrganizationMember> organizationMembers;

    @OneToMany(
            mappedBy = "member",
            cascade=CascadeType.ALL
    )
    private Set<CardMember> cardMembers;

    @OneToMany(
            mappedBy = "member",
            cascade=CascadeType.ALL
    )
    private Set<BoardMembership> boardMemberships;

    protected Member()
    {
        actions = new HashSet<>();
        organizationMembers = new HashSet<>();
        cardMembers = new HashSet<>();
        boardMemberships = new HashSet<>();
    }

    public Member(String id, String memberType, String fullName, String email, Timestamp dateCreated, Timestamp dateLastModified)
    {
        this.id = id;
        this.memberType = memberType;
        this.fullName = fullName;
        this.email = email;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
        actions = new HashSet<>();
        organizationMembers = new HashSet<>();
        cardMembers = new HashSet<>();
        boardMemberships = new HashSet<>();
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getMemberType()
    {
        return memberType;
    }

    public void setMemberType(String memberType)
    {
        this.memberType = memberType;
    }

    public String getFullName()
    {
        return fullName;
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
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

    public Set<Action> getActions()
    {
        return actions;
    }

    public void setActions(Set<Action> actions)
    {
        this.actions = actions;
        for (Action action:actions)
        {
            action.setMember(this);
        }
    }

    public Set<OrganizationMember> getOrganizationMembers()
    {
        return organizationMembers;
    }

    public void setOrganizationMembers(Set<OrganizationMember> organizationMembers)
    {
        this.organizationMembers = organizationMembers;
        for (OrganizationMember organizationMember:organizationMembers)
        {
            organizationMember.setMember(this);
        }
    }

    public Set<CardMember> getCardMembers()
    {
        return cardMembers;
    }

    public void setCardMembers(Set<CardMember> cardMembers)
    {
        this.cardMembers = cardMembers;
        for (CardMember cardMember:cardMembers)
        {
            cardMember.setMember(this);
        }
    }

    public Set<BoardMembership> getBoardMemberships()
    {
        return boardMemberships;
    }

    public void setBoardMemberships(Set<BoardMembership> boardMemberships)
    {
        this.boardMemberships = boardMemberships;
        for (BoardMembership boardMembership:boardMemberships)
        {
            boardMembership.setMember(this);
        }
    }
}
