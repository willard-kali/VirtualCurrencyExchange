package com.business.exchange.model;

import com.business.exchange.domain.User;

import java.util.List;

public class UserResponse extends Response {

    private List<User> users;

    public UserResponse() {
        super();
    }

    public UserResponse(int resultCode, String errDesc) {
        super(resultCode, errDesc);
    }

    public UserResponse(int resultCode, String errDesc, List<User> users) {
        super(resultCode, errDesc);
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
