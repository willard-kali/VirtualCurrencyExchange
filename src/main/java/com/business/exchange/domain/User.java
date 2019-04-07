package com.business.exchange.domain;

import com.business.exchange.model.UserType;

import javax.persistence.*;

/**
 * 用户类
 */
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;

    @Enumerated(EnumType.ORDINAL)
    private UserType userType;

    private String userName;

    private String employeeID;

    private String department;

    private String userGroup;

    private int currencyNumber;

    public User() {
    }

    public User(String employeeID) {
        this.employeeID = employeeID;
    }

    /**
     * 用户ID自动生成，采用默认密码和用户类别，其他字段必填
     * @param userName 用户名
     * @param employeeID 员工工号
     * @param department 部门
     * @param userGroup 小组
     * @param currencyNumber 货币数量
     */
    public User(String userName, String employeeID, String department, String userGroup, int currencyNumber) {
        this.userName = userName;
        this.employeeID = employeeID;
        this.department = department;
        this.userGroup = userGroup;
        this.currencyNumber = currencyNumber;
        setDefaultUserType();
    }

    private void setDefaultUserType() {
        this.userType = UserType.ORDINARY_USER;
    }

    public int getUserId() {
        return userId;
    }

    public UserType getUserType() {
        return userType;
    }

    public String getDepartment() {
        return department;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setCurrencyNumber(int currencyNumber) {
        this.currencyNumber = currencyNumber;
    }

    public int getCurrencyNumber() {
        return currencyNumber;
    }

    public String toRankString() {
        return "{" +
                "\"userName\": \"" + userName + '\"' +
                ", \"employeeID\": \"" + employeeID + '\"' +
                ", \"currencyNumber\": " + currencyNumber +
                '}';
    }

    @Override
    public String toString() {
        return "{" +
                "\"userId\": " + userId +
                ", \"userType\": " + userType.ordinal() +
                ", \"userName\": \"" + userName + '\"' +
                ", \"employeeID\": \"" + employeeID + '\"' +
                ", \"department\": \"" + department + '\"' +
                ", \"userGroup\": \"" + userGroup + '\"' +
                ", \"currencyNumber\": " + currencyNumber +
                '}';
    }
}
