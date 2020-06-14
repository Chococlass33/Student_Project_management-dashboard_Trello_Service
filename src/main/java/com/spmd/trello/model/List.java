package com.spmd.trello.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class List
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String id;
    private String idBoard;
    private String name;
    private Float pos;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;

    public List(String id, String idBoard, String name, Float pos, Timestamp dateCreated, Timestamp dateLastModified)
    {
        this.id = id;
        this.idBoard = idBoard;
        this.name = name;
        this.pos = pos;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getIdBoard()
    {
        return idBoard;
    }

    public void setIdBoard(String idBoard)
    {
        this.idBoard = idBoard;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Float getPos()
    {
        return pos;
    }

    public void setPos(Float pos)
    {
        this.pos = pos;
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
