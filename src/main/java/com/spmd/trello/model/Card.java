package com.spmd.trello.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Card {
    @Id
//    @GeneratedValue(strategy=GenerationType.AUTO)
    private String id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "idList", nullable = false)
    private List list;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "idBoard", nullable = false)
    private Board board;
    private String checkItemStates;
    private int closed;
    private Timestamp dateLastActivity;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String descData;
    private Timestamp due;
    private int dueComplete;
    private String name;
    private Float pos;
    private String shortLink;
    private Timestamp dateCreated;
    private Timestamp dateLastModified;

    @JsonIgnore
    @OneToMany(
            mappedBy = "card",
            cascade = CascadeType.ALL
    )
    private Set<CardMember> cardMembers;

    @JsonIgnore
    @OneToMany(
            mappedBy = "card",
            cascade = CascadeType.ALL
    )
    private Set<CardLabel> cardLabels;

    @JsonIgnore
    @OneToMany(
            mappedBy = "card",
            cascade = CascadeType.ALL
    )
    private Set<Checklist> checklists;

    public Card(String id,
                List list,
                Board board,
                String checkItemStates,
                int closed,
                Timestamp dateLastActivity,
                String description,
                String descData,
                Timestamp due,
                int dueComplete,
                String name,
                Float pos,
                String shortLink,
                Timestamp dateCreated,
                Timestamp dateLastModified) {

        this.list = list;
        this.board = board;
        this.id = id;
        this.checkItemStates = checkItemStates;
        this.closed = closed;
        this.dateLastActivity = dateLastActivity;
        this.description = description;
        this.descData = descData;
        this.due = due;
        this.dueComplete = dueComplete;
        this.name = name;
        this.pos = pos;
        this.shortLink = shortLink;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
        cardMembers = new HashSet<>();
        cardLabels = new HashSet<>();
        checklists = new HashSet<>();
    }

    protected Card() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCheckItemStates() {
        return checkItemStates;
    }

    public void setCheckItemStates(String checkItemStates) {
        this.checkItemStates = checkItemStates;
    }

    public int getClosed() {
        return closed;
    }

    public void setClosed(int closed) {
        this.closed = closed;
    }

    public Timestamp getDateLastActivity() {
        return dateLastActivity;
    }

    public void setDateLastActivity(Timestamp dateLastActivity) {
        this.dateLastActivity = dateLastActivity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescData() {
        return descData;
    }

    public void setDescData(String descData) {
        this.descData = descData;
    }

    public Timestamp getDue() {
        return due;
    }

    public void setDue(Timestamp due) {
        this.due = due;
    }

    public int getDueComplete() {
        return dueComplete;
    }

    public void setDueComplete(int dueComplete) {
        this.dueComplete = dueComplete;
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

    public String getShortLink() {
        return shortLink;
    }

    public void setShortLink(String shortLink) {
        this.shortLink = shortLink;
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

    public Set<CardMember> getCardMembers() {
        return cardMembers;
    }

    public void setCardMembers(Set<CardMember> cardMembers) {
        this.cardMembers = cardMembers;
        for (CardMember cardMember : cardMembers) {
            cardMember.setCard(this);
        }
    }

    public Set<CardLabel> getCardLabels() {
        return cardLabels;
    }

    public void setCardLabels(Set<CardLabel> cardLabels) {
        this.cardLabels = cardLabels;
        for (CardLabel cardLabel : cardLabels) {
            cardLabel.setCard(this);
        }
    }

    public Set<Checklist> getChecklists() {
        return checklists;
    }

    public void setChecklists(Set<Checklist> checklists) {
        this.checklists = checklists;
        this.cardLabels = cardLabels;
        for (Checklist checklist : checklists) {
            checklist.setCard(this);
        }
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    @JsonProperty
    public String getListId() {
        return list.getId();
    }
}
