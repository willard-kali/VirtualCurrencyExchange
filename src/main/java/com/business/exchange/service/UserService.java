package com.business.exchange.service;

import com.business.exchange.domain.User;
import com.business.exchange.model.BaseResponse;
import com.business.exchange.model.LoginResponse;
import com.business.exchange.model.UserProfileResponse;
import com.business.exchange.model.UserResponse;

public interface UserService {

    LoginResponse login(String employeeID, String password);

    BaseResponse create(String userName, String employeeID, String department, String group, int currencyNumber);

    UserProfileResponse info(String employeeID);

//    UserQueryResult query(String employeeID);

    User queryUser(String employeeID);

//    UserQueryResult queryAll();

    String modify();

    BaseResponse password(int userId, String oldPwd, String newPwd);

    UserResponse holdRank();
}
