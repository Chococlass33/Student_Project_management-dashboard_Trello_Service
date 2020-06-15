package com.spmd.trello.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class CardLabel
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    @ManyToOne
    @JoinColumn(name = "idCard",nullable = false)
    private Card card;
    @ManyToOne
    @JoinColumn(name = "idLabel",nullable = false)
    private Label label;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;

    public CardLabel(Card card, Label label, Timestamp dateCreated, Timestamp dateLastModified)
    {
        this.card = card;
        this.label = label;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
    }

    protected CardLabel(){}

    public int getId()
    {
        return id;
    }

    public void setId(int id)
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

    public Card getCard()
    {
        return card;
    }

    public void setCard(Card card)
    {
        this.card = card;
    }

    public Label getLabel()
    {
        return label;
    }

    public void setLabel(Label label)
    {
        this.label = label;
    }
}
