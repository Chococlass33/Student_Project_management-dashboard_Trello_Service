package com.spmd.trello.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Checklist {
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    @ManyToOne
    @JoinColumn(name = "idCard", nullable = false)
    private Card card;
    private String name;
    private Float pos;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;

    @OneToMany(
            mappedBy = "checkList",
            cascade = CascadeType.ALL
    )
    private Set<CheckItem> checkItems;

    public Checklist(String id, Card card, String name, Float pos, Timestamp dateCreated, Timestamp dateLastModified) {
        this.card = card;
        this.id = id;
        this.name = name;
        this.pos = pos;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
        checkItems = new HashSet<>();
    }

    protected Checklist() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPos() {
        return pos;
    }

    public void setPos(Float pos) {
        this.pos = pos;
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

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Set<CheckItem> getCheckItems() {
        return checkItems;
    }

    public void setCheckItems(Set<CheckItem> checkItems) {
        this.checkItems = checkItems;
        for (CheckItem checkItem : checkItems) {
            checkItem.setCheckList(this);
        }
    }
}
