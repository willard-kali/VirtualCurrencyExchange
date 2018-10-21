package com.business.exchange.domain;

import com.business.exchange.constant.UserConstants;

import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;

    @Enumerated(EnumType.ORDINAL)
    private UserType userType;

    private String userName;

    private String employeeID;

    private String password;

    private String department;

    private String userGroup;

    private int currencyNumber;

    public User() {
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
        setDefaultPwd();
        setDefaultUserType();
    }

    private void setDefaultUserType() {
        this.userType = UserType.ORDINARY_USER;
    }

    private void setDefaultPwd() {
        this.password = UserConstants.DEFAULT_PWD;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isValid() {
        return !employeeID.isEmpty();
    }

    @Override
    public String toString() {
        return "{" +
                "userId: " + userId +
                ", userType: " + userType.ordinal() +
                ", userName: '" + userName + '\'' +
                ", employeeID: '" + employeeID + '\'' +
//                ", password: '" + password + '\'' +
                ", department: '" + department + '\'' +
                ", userGroup: '" + userGroup + '\'' +
                ", currencyNumber: " + currencyNumber +
                '}';
    }
}
