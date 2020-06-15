package com.spmd.trello.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class BoardMembership
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String id;
    @ManyToOne
    @JoinColumn(name = "idBoard",nullable = false)
    private Board board;
    @ManyToOne
    @JoinColumn(name = "idMember",nullable = false)
    private Member member;
    private String memberType;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;

    public BoardMembership(String id, String idBoard, String memberType, Timestamp dateCreated, Timestamp dateLastModified)
    {

        this.id = id;
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

    public Member getMember()
    {
        return member;
    }

    public void setMember(Member member)
    {
        this.member = member;
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
