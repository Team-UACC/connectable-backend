package com.backend.connectable.fixture;

public class RequestKeyRequest {

    private BappRequest bapp;
    private String type;

    private RequestKeyRequest() {}

    public RequestKeyRequest(String name, String type) {
        this.bapp = new BappRequest(name);
        this.type = type;
    }
}

class BappRequest {

    private String name;

    public BappRequest(String name) {
        this.name = name;
    }
}
