package com.spmd.trello.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class CardMember
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String id;
    @ManyToOne
    @JoinColumn(name = "idCard",nullable = false)
    private Card card;
    @ManyToOne
    @JoinColumn(name = "idMember",nullable = false)
    private Member member;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;

    public CardMember(String id, Timestamp dateCreated, Timestamp dateLastModified)
    {

        this.id = id;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
    }

    protected CardMember(){}

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

    public Card getCard()
    {
        return card;
    }

    public void setCard(Card card)
    {
        this.card = card;
    }
}
