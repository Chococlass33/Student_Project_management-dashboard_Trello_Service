package com.spmd.trello.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class BoardMembership
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String id;
    private String idBoard;
    private String idMember;
    private String memberType;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;

    public BoardMembership(String id, String idBoard, String idMember, String memberType, Timestamp dateCreated, Timestamp dateLastModified)
    {

        this.id = id;
        this.idBoard = idBoard;
        this.idMember = idMember;
        this.memberType = memberType;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
    }

    protected BoardMembership(){}

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

    public String getIdMember()
    {
        return idMember;
    }

    public void setIdMember(String idMember)
    {
        this.idMember = idMember;
    }

    public String getMemberType()
    {
        return memberType;
    }

    public void setMemberType(String memberType)
    {
        this.memberType = memberType;
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
