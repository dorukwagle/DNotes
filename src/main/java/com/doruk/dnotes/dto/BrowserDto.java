package com.doruk.dnotes.dto;

import com.doruk.dnotes.enums.BrowserElement;

public class BrowserDto {
    private String id;
    private String name;
    private BrowserElement type;

    public BrowserDto(String id, String name, BrowserElement type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BrowserElement getType() {
        return type;
    }
}
