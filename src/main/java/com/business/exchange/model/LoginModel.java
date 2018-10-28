package com.business.exchange.model;

public class LoginModel {

    private String employeeID;

    private String password;

    public LoginModel() {
    }

    public LoginModel(String employeeID, String password) {
        this.employeeID = employeeID;
        this.password = password;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public String getPassword() {
        return password;
    }
}
