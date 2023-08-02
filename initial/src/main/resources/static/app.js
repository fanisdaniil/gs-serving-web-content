// Global variables to store the current highest bidder and bid amount
let highestBidder = '';
let highestBid = 100; // Starting price

// Extract the auctionId from the current URL path (unchanged from before)
function getAuctionIdFromURL() {
    const params = new Proxy(new URLSearchParams(window.location.search), {
        get: (searchParams, prop) => searchParams.get(prop),
    });
    return params.auction_id || 'default_auction_id';
}

// ... Rest of your WebSocket configuration and setup (unchanged from before) ...
const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/gs-guide-websocket'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);


    const auctionId = getAuctionIdFromURL() || "default_auction_id";

    stompClient.subscribe(`/topic/highest-bid/${auctionId}`, (message) => {
        console.log(message.body);
        const jsonPayload = JSON.parse(message.body);
        const bidMessage = JSON.parse(jsonPayload);
        showHighestBid(bidMessage);
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

// Handle placing a bid
function placeBid() {
    const name = document.getElementById('name').value;
    const bidAmount = parseInt(document.getElementById('bidAmount').value);

    if (name && bidAmount > highestBid) {

        const message = {
            name: name,
            bid: bidAmount
        };

        const auctionId = getAuctionIdFromURL() || 'default_auction_id';

        stompClient.publish({
            destination: `/app/bid/${auctionId}`,
            body: JSON.stringify(message)
        });
    } else {
        // Handle invalid bid (e.g., lower than current highest bid or missing name)
        alert('Invalid bid. Please enter a valid bid amount higher than the current highest bid and provide your name.');
    }
}

// Show the current highest bid in the table
function showHighestBid(bidMessage) {
    $("#bids").html(`
        <tr>
            <td>${bidMessage.bidder}</td>
            <td>${bidMessage.highestBid}</td>
        </tr>
    `);

    highestBidder = bidder;
    highestBid = highestBid;
}

// Bind the placeBid() function to the "Place Bid" button click event
$(function () {
    $("#placeBid").click(() => placeBid());
    $("form").on('submit', (e) => e.preventDefault());
    $("#connect").click(() => connect());
    $("#disconnect").click(() => disconnect());
});
