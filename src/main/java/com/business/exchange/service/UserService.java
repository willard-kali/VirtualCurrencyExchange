package com.business.exchange.service;

import com.business.exchange.domain.*;
import com.business.exchange.model.LoginResponse;
import com.business.exchange.model.Response;
import com.business.exchange.model.UserResponse;

public interface UserService {

    LoginResponse login(String employeeID, String password);

    String create(String userName, String employeeID, String department, String group, int currencyNumber);

//    UserQueryResult query(String employeeID);

    User queryUser(String employeeID);

//    UserQueryResult queryAll();

    String modify();

    Response password(String employeeID, String oldPwd, String newPwd);

    UserResponse holdRank();
}
