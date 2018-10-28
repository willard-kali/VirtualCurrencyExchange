package com.business.exchange.service;

import com.business.exchange.constant.RespDefine;
import com.business.exchange.constant.UserConstants;
import com.business.exchange.controller.UserController;
import com.business.exchange.domain.*;
import com.business.exchange.model.BaseResponse;
import com.business.exchange.model.LoginResponse;
import com.business.exchange.model.UserProfileResponse;
import com.business.exchange.model.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private PasswordRepository passwordRepository;

    @Override
    public LoginResponse login(String employeeID, String password) {
        LoginResponse loginResponse = new LoginResponse(RespDefine.DESC_LOGIN_ERROR, "");
        User user = userRepository.findByEmployeeID(employeeID);
        if (null == user) {
            LOGGER.error("no valid user from employeeID: {}.", employeeID);
            return loginResponse;
        }
        int userId = user.getUserId();

        Password userPwd = passwordRepository.findByUserId(userId);

        if (null == userPwd) {
            LOGGER.error("user {} password error.", userId);
            return loginResponse;
        }

        if (userPwd.getPassword().equals(password)) {
            loginResponse = new LoginResponse(RespDefine.DESC_LOGIN_OK, user.getUserType().getValue());
        } else {
            LOGGER.error("login password error.");
        }

        return loginResponse;
    }

    @Override
    public UserProfileResponse info(String employeeID) {
        UserProfileResponse userInfoResp = new UserProfileResponse(RespDefine.ERR_CODE_GET_USER_INFO_FAILED,
                RespDefine.ERR_DESC_GET_USER_INFO_FAILED);
        User user = userRepository.findByEmployeeID(employeeID);

        if (null == user.getEmployeeID() || user.getEmployeeID().isEmpty() || user.getUserId() <= 0) {
            LOGGER.error("user invalid.");
            return userInfoResp;
        }

        int userId = user.getUserId();

        List<Business> businessResponse = businessRepository
                .findAllBySrcUserIdEqualsOrDestUserIdEqualsOrderByExchangeDateDesc(userId, userId);

        if (null == businessResponse) {
            LOGGER.error("business response error.");
            return userInfoResp;
        }

        List<User> ownRankUsers = userRepository.findAll(
                Sort.by(
                        Sort.Order.desc("currencyNumber"),
                        Sort.Order.desc("userId")
                )
        );

        int total = ownRankUsers.size();
        int rank = 1;
        for (User holdRankUser : ownRankUsers) {
            if (holdRankUser.getEmployeeID() != null && user.getEmployeeID().equals(holdRankUser.getEmployeeID())) {
                break;
            } else {
                rank++;
            }
        }

        userInfoResp = new UserProfileResponse(RespDefine.CODE_SUCCESS, RespDefine.DESC_SUCCESS,
                user, rank, total, businessResponse.size());

        return userInfoResp;
    }

    @Override
    public BaseResponse create(String userName, String employeeID, String department, String group, int currencyNumber) {
        BaseResponse createResponse = new BaseResponse(RespDefine.ERR_CODE_USER_CREATE_FAILED,
                RespDefine.ERR_DESC_USER_CREATE_FAILED);

        boolean confirmUser = userRepository.existsByEmployeeID(employeeID);
        LOGGER.debug("confirm user is: {}.", confirmUser);
        if (confirmUser) {
            LOGGER.error("employeeID: {} is exist.", employeeID);
            return createResponse;
        }

        User user = new User(userName, employeeID, department, group, currencyNumber);
        User createdUser = userRepository.saveAndFlush(user);
        int userId = createdUser.getUserId();

        Password pwd = new Password(userId, UserConstants.DEFAULT_PWD);
        passwordRepository.saveAndFlush(pwd);

        createResponse = new BaseResponse(RespDefine.CODE_SUCCESS, RespDefine.DESC_SUCCESS);

        return createResponse;
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
     * @param userId 用户ID
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @return 修改是否成功
     */
    @Override
    public BaseResponse password(int userId, String oldPwd, String newPwd) {
        BaseResponse modifyPwdResponse = new BaseResponse(RespDefine.ERR_CODE_PASSWORD_MODIFY_FAILED,
                RespDefine.ERR_DESC_PASSWORD_MODIFY_FAILED);
        Password pwd = passwordRepository.findByUserId(userId);

        if (null == pwd || null == pwd.getPassword() || pwd.getPassword().isEmpty()) {
            LOGGER.error("password info for user {} error.", userId);
            return modifyPwdResponse;
        }

        if (pwd.getPassword().equals(oldPwd)) {
            pwd.setPassword(newPwd);
            passwordRepository.saveAndFlush(pwd);
            modifyPwdResponse = new BaseResponse(RespDefine.CODE_SUCCESS, RespDefine.DESC_SUCCESS);
        } else {
            LOGGER.error("old password input error.");
        }

        return modifyPwdResponse;
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
