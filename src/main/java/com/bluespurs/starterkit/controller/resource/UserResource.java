package com.bluespurs.starterkit.controller.resource;

import org.springframework.hateoas.ResourceSupport;

public class UserResource extends ResourceSupport {
    private long userId;
    private String email;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long id) {
        this.userId = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
