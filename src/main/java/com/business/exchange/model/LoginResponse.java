package com.business.exchange.model;

/**
 * login response
 */
public class LoginResponse {
    private String status;

    private String currentAuthority;

    public LoginResponse() {
    }

    public LoginResponse(String status, String currentAuthority) {
        this.status = status;
        this.currentAuthority = currentAuthority;
    }

    public String getStatus() {
        return status;
    }

    public String getCurrentAuthority() {
        return currentAuthority;
    }
}
