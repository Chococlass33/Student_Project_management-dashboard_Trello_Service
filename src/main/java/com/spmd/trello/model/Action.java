package com.spmd.trello.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Action {
    /* Database related fields */
    @Id
    private String id;
    private String member;
    private String type;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String data;
    private Timestamp dateCreated;
    private String board;

    protected Action() {
    }

    public Action(String id, String board, String member, String type, String data, Timestamp dateCreated) {
        this.id = id;
        this.board = board;
        this.member = member;
        this.type = type;
        this.data = data;
        this.dateCreated = dateCreated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    @Override
    public String toString() {
        return "Action{" +
                "id='" + id + '\'' +
                ", member=" + member +
                ", type='" + type + '\'' +
                ", data='" + data + '\'' +
                ", dateCreated=" + dateCreated +
                '}';
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }
}
