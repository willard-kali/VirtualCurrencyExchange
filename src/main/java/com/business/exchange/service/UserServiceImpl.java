package com.business.exchange.service;

import com.business.exchange.constant.RespDefine;
import com.business.exchange.controller.UserController;
import com.business.exchange.domain.*;
import com.business.exchange.model.LoginResponse;
import com.business.exchange.model.Response;
import com.business.exchange.model.UserQueryResult;
import com.business.exchange.model.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
    public LoginResponse login(String employeeID, String password) {
        LoginResponse response = new LoginResponse(RespDefine.DESC_LOGIN_ERROR, "");
        User user = userRepository.findByEmployeeID(employeeID);
        if (null == user) {
            LOGGER.warn("no valid user from employeeID: {}.", employeeID);
        } else if (user.getPassword().equals(password)){
            response = new LoginResponse(RespDefine.DESC_LOGIN_OK, user.getUserType().getValue());
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

    /*@Override
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
    }*/

    @Override
    public User queryUser(String employeeID) {
        User user = userRepository.findByEmployeeID(employeeID);
        if (null == user) {
            LOGGER.warn("no user info by query: {}.", employeeID);
            return new User();
        } else {
            return user;
        }
    }

    /*@Override
    public UserQueryResult queryAll() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            LOGGER.warn("no user info by query all.");
            return new UserQueryResult(0, new ArrayList<>());
        } else {
            return new UserQueryResult(users.size(), users);
        }
    }*/

    @Override
    public String modify() {
        return null;
    }

    /**
     * 用户修改密码
     * @param employeeID 工号
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @return 修改是否成功
     */
    @Override
    public Response password(String employeeID, String oldPwd, String newPwd) {
        Response modifyPwdResponse = new Response(RespDefine.ERR_CODE_PASSWORD_MODIFY_FAILED,
                RespDefine.ERR_DESC_PASSWORD_MODIFY_FAILED);
        User user = userRepository.findByEmployeeID(employeeID);
        if (null == user || null == user.getPassword() || user.getPassword().isEmpty()) {
            LOGGER.error("employeeID is incorrect.");
            return modifyPwdResponse;
        }

        if (user.getPassword().equals(oldPwd)) {
            user.setPassword(newPwd);
            userRepository.saveAndFlush(user);
            modifyPwdResponse = new Response(RespDefine.CODE_SUCCESS, RespDefine.DESC_SUCCESS);
            return modifyPwdResponse;
        } else {
            LOGGER.error("old password input incorrect.");
            return modifyPwdResponse;
        }
    }

    /**
     * 虚拟货币持有量排行
     * @return 用户排行榜
     */
    @Override
    public UserResponse holdRank() {

        UserResponse holdRankUsersResp = new UserResponse(RespDefine.ERR_CODE_QUERY_HOLD_RANK_FAILED,
                RespDefine.ERR_DESC_QUERY_HOLD_RANK_FAILED);

        List<User> ownRankUsers = userRepository.findAll(
                Sort.by(
                        Sort.Order.desc("currencyNumber"),
                        Sort.Order.desc("userId")
                )
        );

        if (null == ownRankUsers || ownRankUsers.size() == 0) {
            LOGGER.error("current system's users is empty.");
            return holdRankUsersResp;
        }

        holdRankUsersResp = new UserResponse(RespDefine.CODE_SUCCESS, RespDefine.DESC_SUCCESS, ownRankUsers);

        return holdRankUsersResp;
    }

}
