package com.spmd.trello.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
public class List {
    @Id
//    @GeneratedValue(strategy=GenerationType.AUTO)
    private String id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "idBoard", nullable = false)
    private Board board;
    private String name;
    private Float pos;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;

    @JsonIgnore
    @OneToMany(
            mappedBy = "list",
            cascade = CascadeType.ALL
    )
    private Set<Card> cards;

    protected List() {}

    public List(String id, Board board, String name, Float pos, Timestamp dateCreated, Timestamp dateLastModified) {
        this.id = id;
        this.name = name;
        this.pos = pos;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
        cards = new HashSet<>();
        this.board = board;
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

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
        for (Card card : cards) {
            card.setList(this);
        }
    }
}
