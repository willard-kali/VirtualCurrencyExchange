package com.business.exchange.domain;

import java.util.List;

public class QueryUserResp {
    private int size;

    private List<User> users;

    public QueryUserResp(int size, List<User> users) {
        this.size = size;
        this.users = users;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
