package com.spmd.trello.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Action
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String id;
    @ManyToOne
    @JoinColumn(name = "idMember",nullable = false)
    private Member member;
    private String type;
    private String data;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;




    protected Action() {}

    public Action(Member member, String type, String data, Timestamp dateCreated, Timestamp dateLastModified)
    {
        this.type = type;
        this.data = data;
        this.dateCreated = dateCreated;
        this. dateLastModified = dateLastModified;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
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
}
