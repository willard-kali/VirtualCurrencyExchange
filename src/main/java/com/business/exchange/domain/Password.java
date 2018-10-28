package com.business.exchange.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Password {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int passwordId;

    private int userId;

    private String password;

    public Password() {
    }

    public Password(int userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public int getPasswordId() {
        return passwordId;
    }

    public int getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
