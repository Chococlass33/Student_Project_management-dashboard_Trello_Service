package com.spmd.trello.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Label
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String id;
    @ManyToOne
    @JoinColumn(name = "idBoard",nullable = false)
    private Board board;
    private String name;
    private String colour;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;

    @OneToMany(
            mappedBy = "label",
            cascade=CascadeType.ALL
    )
    private Set<CardLabel> cardLabels;

    public Label(String id, String idBoard, String name, String colour, Timestamp dateCreated, Timestamp dateLastModified)
    {
        this.id = id;
        this.name = name;
        this.colour = colour;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
        cardLabels = new HashSet<>();
    }

    protected Label(){}

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

    public String getColour()
    {
        return colour;
    }

    public void setColour(String colour)
    {
        this.colour = colour;
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

    public Set<CardLabel> getCardLabels()
    {
        return cardLabels;
    }

    public void setCardLabels(Set<CardLabel> cardLabels)
    {
        this.cardLabels = cardLabels;
        for (CardLabel cardLabel:cardLabels)
        {
            cardLabel.setLabel(this);
        }
    }

    public Board getBoard()
    {
        return board;
    }

    public void setBoard(Board board)
    {
        this.board = board;
    }
}
