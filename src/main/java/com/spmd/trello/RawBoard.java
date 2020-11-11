package com.spmd.trello;

/**
 * The raw trello board data returned from the api
 */
public class RawBoard {
    public String name;
    public RawCard[] cards;
    public RawList[] lists;

    /**
     * The raw trello care data returned from the api
     */
    public static class RawCard {
        public String id;
        public String desc;
        public String idList;
        public String name;
        public float pos;
        public boolean closed;
    }

    /**
     * The raw trello list data returned from the api
     */
    public static class RawList {
        public String id;
        public boolean closed;
        public String name;
        public float pos;
    }
}
