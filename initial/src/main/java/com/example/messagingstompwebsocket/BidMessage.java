package com.example.messagingstompwebsocket;

public class BidMessage {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    private int bid;

    public BidMessage() {
    }

    public BidMessage(String name, int bid) {
        this.bid = bid;
        this.name = name;
    }



    String getName() {
        return name;
    }

    int getBid() {
        return bid;
    }

    @Override
    public String toString() {
        return "BidMessage{" +
                "name='" + name + '\'' +
                ", bid=" + bid +
                '}';
    }
}