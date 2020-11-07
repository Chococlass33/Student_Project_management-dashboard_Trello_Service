package com.spmd.trello.database;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Member
{
    /* Database related fields */
    @Id
    private String id;
    private String member;
    private String type;
    private String email;
    private Timestamp dateCreated;

    protected Member() {
    }

    public Member(String id, String member, String type, String email, Timestamp dateCreated) {
        this.id = id;
        this.member = member;
        this.type = type;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
                ", email='" + email + '\'' +
                ", dateCreated=" + dateCreated +
                '}';
    }

}
