package com.spmd.trello.trelloModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Check;

import java.util.List;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrelloBoard {
    public String id;
    public String desc;
    public Optional<Object> descData;
    public String shortUrl;
    public String name;
    public List<Card> cards;
    public List<Label> labels;
    public List<Member> members;
    public List<Checklist> checklists;
    public List<TrelloList> lists;

    @Override
    public String toString() {
        return "TrelloBoard{" +
                "id='" + id + '\'' +
                ", desc='" + desc + '\'' +
                ", descData='" + descData + '\'' +
                ", shortUrl='" + shortUrl + '\'' +
                ", name='" + name + '\'' +
                ", cards='" + cards + '\'' +
                ", labels='" + labels + '\'' +
                ", members='" + members + '\'' +
                ", checklists='" + checklists + '\'' +
                ", lists='" + lists + '\'' +
                '}';
    }

    public static class Label {
        public String id;
        public String name;
        public String color;
        public String idBoard;

        @Override
        public String toString() {
            return "Label{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", color='" + color + '\'' +
                    ", idBoard='" + idBoard + '\'' +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Card {
        public String id;
        public String idBoard;
        public String idList;
        public Optional<List<String>> checkItemStates;
        public boolean closed;
        public String dateLastActivity;
        public String desc;
        public Optional<Object> descData;
        public Optional<String> due;
        public String name;
        public float pos;
        public String shortUrl;
        public List<String> idLabels;
        public List<String> idMembers;

        @Override
        public String toString() {
            return "Card{" +
                    "id='" + id + '\'' +
                    ", idBoard='" + idBoard + '\'' +
                    ", idList='" + idList + '\'' +
                    ", checkItemStates=" + checkItemStates +
                    ", closed=" + closed +
                    ", dateLastActivity='" + dateLastActivity + '\'' +
                    ", desc='" + desc + '\'' +
                    ", descData='" + descData + '\'' +
                    ", due='" + due + '\'' +
                    ", name='" + name + '\'' +
                    ", pos=" + pos +
                    ", shortUrl='" + shortUrl + '\'' +
                    ", idLabels='" + idLabels + '\'' +
                    ", idMembers='" + idMembers + '\'' +
                    '}';
        }
    }

    public static class Member {
        public String id;
        public String memberType;
        public String fullName;
        public String email;

        @Override
        public String toString() {
            return "Member{" +
                    "id='" + id + '\'' +
                    ", memberType='" + memberType + '\'' +
                    ", fullName='" + fullName + '\'' +
                    ", email='" + email + '\'' +
                    '}';
        }
    }

    public static class Checklist {
        public String id;
        public String idCard;
        public String name;
        public float pos;
        public List<CheckItem> checkItems;

        @Override
        public String toString() {
            return "Checklist{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", idCard='" + idCard + '\'' +
                    ", pos=" + pos +
                    ", checkItems=" + checkItems +
                    '}';
        }

        public static class CheckItem {
            public String idChecklist;
            public String state;
            public String id;
            public String name;
            public Optional<Object> nameData;
            public float pos;

            @Override
            public String toString() {
                return "CheckItem{" +
                        "idChecklist='" + idChecklist + '\'' +
                        ", state='" + state + '\'' +
                        ", id='" + id + '\'' +
                        ", name='" + name + '\'' +
                        ", nameData=" + nameData +
                        ", pos=" + pos +
                        '}';
            }
        }
    }

    public static class TrelloList {
        public String id;
        public String idBoard;
        public String name;
        public float pos;

        @Override
        public String toString() {
            return "TrelloList{" +
                    "id='" + id + '\'' +
                    ", idBoard='" + idBoard + '\'' +
                    ", name='" + name + '\'' +
                    ", pos='" + pos + '\'' +
                    '}';
        }
    }
}
