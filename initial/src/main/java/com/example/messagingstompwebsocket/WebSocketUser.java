package com.example.messagingstompwebsocket;

import java.security.Principal;

public class WebSocketUser implements Principal {
    private final String name;

    public WebSocketUser(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}