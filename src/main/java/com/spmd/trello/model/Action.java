package com.spmd.trello.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Action {
    /* Database related fields */
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    @ManyToOne
    @JoinColumn(name = "idMember", nullable = false)
    private Member member;
    private String type;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String data;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;

    protected Action() {
    }

    public Action(String id, Member member, String type, String data, Timestamp dateCreated, Timestamp dateLastModified) {
        this.id = id;
        this.member = member;
        this.type = type;
        this.data = data;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
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

    public Timestamp getDateLastModified() {
        return dateLastModified;
    }

    public void setDateLastModified(Timestamp dateLastModified) {
        this.dateLastModified = dateLastModified;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
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
                ", dateLastModified=" + dateLastModified +
                '}';
    }
}
