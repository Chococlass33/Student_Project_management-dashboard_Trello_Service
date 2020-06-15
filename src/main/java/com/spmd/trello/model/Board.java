package com.spmd.trello.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Board
{
    @Id
//    @GeneratedValue(strategy=GenerationType.AUTO)
    private String id;
    private String name;
    private String description;
    private String descData;
    private String shortLink;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;

    @OneToMany(
            mappedBy = "board",
            cascade=CascadeType.ALL
    )
    private Set<BoardMembership> boardMemberships;

    @OneToMany(
            mappedBy = "board",
            cascade=CascadeType.ALL
    )
    private Set<Label> labels;

    @OneToMany(
            mappedBy = "board",
            cascade=CascadeType.ALL
    )
    private Set<Card> cards;

    @OneToMany(
            mappedBy = "board",
            cascade=CascadeType.ALL
    )
    private Set<List> lists;

    @OneToMany(
            mappedBy = "board",
            cascade = CascadeType.ALL
    )
    private Set<Action> actions;

    public Board(String id, String name, String description, String descData, String shortLink, Timestamp dateCreated, Timestamp dateLastModified)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.descData = descData;
        this.shortLink = shortLink;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
        boardMemberships = new HashSet<>();
        labels = new HashSet<>();
        cards = new HashSet<>();
        lists = new HashSet<>();
        actions = new HashSet<>();
    }

    protected Board(){}

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

    public String getShortLink()
    {
        return shortLink;
    }

    public void setShortLink(String shortLink)
    {
        this.shortLink = shortLink;
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

    public Set<BoardMembership> getBoardMemberships()
    {
        return boardMemberships;
    }

    public void setBoardMemberships(Set<BoardMembership> boardMemberships)
    {
        this.boardMemberships = boardMemberships;
        for (BoardMembership boardMembership:boardMemberships)
        {
            boardMembership.setBoard(this);
        }
    }

    public Set<Label> getLabels()
    {
        return labels;
    }

    public void setLabels(Set<Label> labels)
    {
        this.labels = labels;
        for (Label label:labels)
        {
            label.setBoard(this);
        }
    }

    public Set<Card> getCards()
    {
        return cards;
    }

    public void setCards(Set<Card> cards)
    {
        this.cards = cards;
        for (Card card:cards)
        {
            card.setBoard(this);
        }
    }

    public Set<List> getLists()
    {
        return lists;
    }

    public void setLists(Set<List> lists)
    {
        this.lists = lists;
        for (List list:lists)
        {
            list.setBoard(this);
        }
    }

    public Set<Action> getActions() {
        return actions;
    }

    public void setActions(Set<Action> actions) {
        this.actions = actions;
    }
}
