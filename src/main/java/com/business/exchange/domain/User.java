package com.business.exchange.domain;

import com.business.exchange.constant.UserConstants;

public class User {
    private int userId;

    private UserType userType;

    private String userName;

    private String employeeID;

    private String password;

    private String department;

    private String group;

    private int currencyNumber;

    /**
     * 用户ID自动生成，采用默认密码和用户类别，其他字段必填
     * @param userName 用户名
     * @param employeeID 员工工号
     * @param department 部门
     * @param group 小组
     * @param currencyNumber 货币数量
     */
    public User(String userName, String employeeID, String department, String group, int currencyNumber) {
        this.userName = userName;
        this.employeeID = employeeID;
        this.department = department;
        this.group = group;
        this.currencyNumber = currencyNumber;
        setDefaultPwd();
        setDefaultUserType();
    }

    private void setDefaultUserType() {
        this.userType = UserType.ORDINARY_USER;
    }

    private void setDefaultPwd() {
        this.password = UserConstants.DEFAULT_PWD;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userType=" + userType +
                ", userName='" + userName + '\'' +
                ", employeeID='" + employeeID + '\'' +
                ", password='" + password + '\'' +
                ", department='" + department + '\'' +
                ", group='" + group + '\'' +
                ", currencyNumber=" + currencyNumber +
                '}';
    }
}
