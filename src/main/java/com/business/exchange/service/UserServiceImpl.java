package com.business.exchange.service;

import com.business.exchange.controller.UserController;
import com.business.exchange.domain.BaseResponse;
import com.business.exchange.domain.UserQueryResult;
import com.business.exchange.domain.User;
import com.business.exchange.domain.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.business.exchange.constant.UserConstants.*;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public User valid(String employeeID, String password) {
        User user = userRepository.findByEmployeeID(employeeID);
        if (null == user) {
            LOGGER.warn("no valid user from employeeID: {}.", employeeID);
            return new User();
        } else if (user.getPassword().equals(password)){
            return user;
        } else {
            LOGGER.error("password incorrect for employeeID: {}.", employeeID);
            return new User();
        }
    }

    @Override
    public BaseResponse login(String employeeID, String password) {
        BaseResponse response = new BaseResponse(false);
        User user = userRepository.findByEmployeeID(employeeID);
        if (null == user) {
            LOGGER.warn("no valid user from employeeID: {}.", employeeID);
        } else if (user.getPassword().equals(password)){
            response.setOk(true);
            response.setMessage("login success");
            response.setData(user);
        } else {
            LOGGER.error("password incorrect for employeeID: {}.", employeeID);
        }
        return response;
    }

    @Override
    public String create(String userName, String employeeID, String department, String group, int currencyNumber) {
        User user = new User(userName, employeeID, department, group, currencyNumber);
        userRepository.saveAndFlush(user);
        return CREATE_SUCCESS;
    }

    @Override
    public UserQueryResult query(String employeeID) {
        User user = userRepository.findByEmployeeID(employeeID);
        if (null == user) {
            LOGGER.warn("no user info by query: {}.", employeeID);
            return new UserQueryResult(0, new ArrayList<>());
        } else {
            List<User> users = new ArrayList<User>();
            users.add(user);
            return new UserQueryResult(users.size(), users);
        }
    }

    @Override
    public UserQueryResult queryAll() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            LOGGER.warn("no user info by query all.");
            return new UserQueryResult(0, new ArrayList<>());
        } else {
            return new UserQueryResult(users.size(), users);
        }
    }

    @Override
    public String modify() {
        return null;
    }

    @Override
    public String password(String employeeID, String oldPwd, String newPwd) {
        String passwordStatus = PWD_FAILED;
        User user = userRepository.findByEmployeeID(employeeID);
        if (null == user) {
            LOGGER.error("employeeID incorrect.");
            return passwordStatus;
        }
        if (user.getPassword().equals(oldPwd)) {
            user.setPassword(newPwd);
            userRepository.saveAndFlush(user);
            passwordStatus = PWD_SUCCESS;
        } else {
            LOGGER.error("old password incorrect.");
        }
        return passwordStatus;
    }


}
