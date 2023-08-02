package com.example.messagingstompwebsocket;

public class HighestBidResponse {
    private final String bidder;
    private final int highestBid;

    public HighestBidResponse(String bidder, int highestBid) {
        this.bidder = bidder;
        this.highestBid = highestBid;
    }

    // Getters and setters (or use Lombok for convenience)
    // ...

    @Override
    public String toString() {
        return "HighestBidResponse{" +
                "bidder='" + bidder + '\'' +
                ", highestBid=" + highestBid +
                '}';
    }
}
