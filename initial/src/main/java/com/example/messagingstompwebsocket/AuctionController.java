package com.example.messagingstompwebsocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AuctionController {

    // Map to store the highest bid for each auction (key: auctionId, value: highest bid amount)
    private final Map<String, Integer> highestBids = new HashMap<>();

    @MessageMapping("/bid/{auction_id}")
    @SendTo("/topic/highest-bid/{auction_id}")
    public String placeBid(@DestinationVariable("auction_id") String auctionId, BidMessage bidMessage) throws JsonProcessingException {

        // Ensure the auctionId is valid (e.g., not empty or null)
        if (auctionId == null || auctionId.isEmpty()) {
            throw new IllegalArgumentException("Invalid auctionId");
        }

        // Extract the username from the WebSocket session
        String username = bidMessage.getName();

        // Get the current highest bid for the auction
        int currentHighestBid = highestBids.getOrDefault(auctionId, 100); // Starting price is 100

        // Check if the new bid is higher than the current highest bid
        if (bidMessage.getBid() > currentHighestBid) {
            highestBids.put(auctionId, bidMessage.getBid());
            return JsonUtils.serializeObjectToString(new HighestBidResponse(username, bidMessage.getBid()));
        } else {
            // If the bid is not higher, do not update the highest bid
            return JsonUtils.serializeObjectToString(new HighestBidResponse(username, currentHighestBid));
        }
    }
}
