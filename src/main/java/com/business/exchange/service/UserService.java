package com.business.exchange.service;

import com.business.exchange.domain.BaseResponse;
import com.business.exchange.domain.User;
import com.business.exchange.domain.UserQueryResult;

public interface UserService {

    User valid(String employeeID, String password);

    BaseResponse login(String employeeID, String password);

    String create(String userName, String employeeID, String department, String group, int currencyNumber);

    UserQueryResult query(String employeeID);

    UserQueryResult queryAll();

    String modify();

    String password(String employeeID, String oldPwd, String newPwd);
}
