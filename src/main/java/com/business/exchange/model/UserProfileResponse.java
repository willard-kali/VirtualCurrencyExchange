package com.business.exchange.model;

import com.business.exchange.domain.User;

public class UserProfileResponse extends Response {

    private User user;

    private int currentRank;

    private int userTotalNumber;

    private int exchangeTotalNumber;

    public UserProfileResponse() {
        super();
    }

    public UserProfileResponse(int resultCode, String errDesc) {
        super(resultCode, errDesc);
    }

    public UserProfileResponse(int resultCode, String errDesc, User user, int currentRank, int userTotalNumber, int exchangeTotalNumber) {
        super(resultCode, errDesc);
        this.user = user;
        this.currentRank = currentRank;
        this.userTotalNumber = userTotalNumber;
        this.exchangeTotalNumber = exchangeTotalNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getCurrentRank() {
        return currentRank;
    }

    public void setCurrentRank(int currentRank) {
        this.currentRank = currentRank;
    }

    public int getUserTotalNumber() {
        return userTotalNumber;
    }

    public void setUserTotalNumber(int userTotalNumber) {
        this.userTotalNumber = userTotalNumber;
    }

    public int getExchangeTotalNumber() {
        return exchangeTotalNumber;
    }

    public void setExchangeTotalNumber(int exchangeTotalNumber) {
        this.exchangeTotalNumber = exchangeTotalNumber;
    }

    /*@Override
    public String toString() {
        return "UserProfileResponse{" +
                "resultCode=" + super.getResultCode() +
                ", errDesc=\"" + super.getErrDesc() + "\"" +
                ", user=" + user +
                ", currentRank=" + currentRank +
                ", userTotalNumber=" + userTotalNumber +
                ", exchangeTotalNumber=" + exchangeTotalNumber +
                '}';
    }*/
}
