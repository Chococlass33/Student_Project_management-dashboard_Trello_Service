package com.spmd.trello.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class CardLabel
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String id;
    @ManyToOne
    @JoinColumn(name = "idCard",nullable = false)
    private Card card;
    private String idLabel;
    @ManyToOne
    @JoinColumn(name = "idLabel",nullable = false)
    private Label label;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;

    public CardLabel(String id, String idCard, String idLabel, Timestamp dateCreated, Timestamp dateLastModified)
    {

        this.id = id;
        this.idLabel = idLabel;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
    }

    protected CardLabel(){}

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getIdLabel()
    {
        return idLabel;
    }

    public void setIdLabel(String idLabel)
    {
        this.idLabel = idLabel;
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
