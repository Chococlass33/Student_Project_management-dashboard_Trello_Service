package com.spmd.trello.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class Action
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String id;
    private String idMember;
    private String type;
    private String data;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;

    protected Action() {}

    public Action(String idMember, String type, String data, Timestamp dateCreated, Timestamp dateLastModified)
    {
        this.idMember = idMember;
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

    public String getIdMember()
    {
        return idMember;
    }

    public void setIdMember(String idMember)
    {
        this.idMember = idMember;
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
}
