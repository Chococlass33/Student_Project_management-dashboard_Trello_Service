package com.spmd.trello.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class CheckItem
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String id;
    private String idCheckList;
    private String name;
    private String nameData;
    private Float pos;
    private String state;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;

    public CheckItem(String id, String idCheckList, String name, String nameData, Float pos, String state, Timestamp dateCreated, Timestamp dateLastModified)
    {
        this.id = id;
        this.idCheckList = idCheckList;
        this.name = name;
        this.nameData = nameData;
        this.pos = pos;
        this.state = state;
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

    public String getIdCheckList()
    {
        return idCheckList;
    }

    public void setIdCheckList(String idCheckList)
    {
        this.idCheckList = idCheckList;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getNameData()
    {
        return nameData;
    }

    public void setNameData(String nameData)
    {
        this.nameData = nameData;
    }

    public Float getPos()
    {
        return pos;
    }

    public void setPos(Float pos)
    {
        this.pos = pos;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
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
