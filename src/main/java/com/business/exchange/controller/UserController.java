package com.business.exchange.controller;

import com.business.exchange.domain.BaseResponse;
import com.business.exchange.domain.User;
import com.business.exchange.domain.UserQueryResult;
import com.business.exchange.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

import static com.business.exchange.constant.UserConstants.*;

/**
 * User Management Request
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "valid", method = RequestMethod.GET)
    public String valid(String employeeID, String password) {
        String validStatus = VALID_FAILED;
        if (null == employeeID || employeeID.isEmpty()) {
            LOGGER.error("employeeID incorrect.");
        } else if (null == password || password.isEmpty()) {
            LOGGER.error("password incorrect.");
        } else {
            LOGGER.info("params check valid, begin to valid.");
//            validStatus = userService.valid(employeeID, password);
            LOGGER.info("valid user finished, status is: {}", validStatus);
        }
        return validStatus;
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public BaseResponse login(String employeeID, String password, HttpSession session) {

        BaseResponse response = userService.login(employeeID, password);
        if (response.isOk()) {
            session.setAttribute(session.getId(), response.getData().toString());
        }
        return response;
    }

    /**
     * create one user
     * @param userName user name
     * @param employeeID employee id
     * @param department department
     * @param group user group
     * @param currencyNumber user initial currency number
     * @return create result
     */
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String create(@RequestParam("userName") String userName,
                         @RequestParam("employeeID") String employeeID,
                         @RequestParam("department") String department,
                         @RequestParam("group") String group,
                         @RequestParam("currencyNumber") int currencyNumber) {
        String createStatus = CREATE_FAILED;

        if (null == userName || "".equals(userName) || userName.length() > USERNAME_MAX_LENGTH) {
            LOGGER.error("userName incorrect.");
        } else if (null == employeeID || "".equals(employeeID) || employeeID.length() > EMPLOYEE_ID_MAX_LENGTH) {
            LOGGER.error("employeeID incorrect.");
        } else if (null == department || "".equals(department)) {
            LOGGER.error("department incorrect.");
        } else if (null == group || "".equals(group)) {
            LOGGER.error("group incorrect.");
        } else if (currencyNumber < 0 || !isValidCurrencyNumber(currencyNumber)) {
            LOGGER.error("currencyNumber incorrect.");
        } else {
            LOGGER.info("params check valid, begin to create.");
            createStatus = userService.create(userName, employeeID, department, group, currencyNumber);
            LOGGER.info("create user finished, status is: {}", createStatus);
        }
        return createStatus;
    }


    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public String getUserInfo(@RequestParam("employeeID") String employeeID) {
        if (null == employeeID || "".equals(employeeID) || employeeID.length() > EMPLOYEE_ID_MAX_LENGTH) {
            LOGGER.error("employeeID not correct.");
            return new UserQueryResult(0, new ArrayList<>()).toString();
        }
        UserQueryResult users = userService.query(employeeID);
        return users.toString();
    }

    @RequestMapping(value = "/query_all", method = RequestMethod.GET)
    public String getUserInfo() {
        UserQueryResult users = userService.queryAll();
        return users.toString();
    }

    @RequestMapping(value = "/modify", method = RequestMethod.GET)
    public String modifyUserInfo(@RequestParam("user") User user) {
        //todo
        return "";
    }

    @RequestMapping(value = "/password", method = RequestMethod.GET)
    public String modifyPassword(@RequestParam("employeeID") String employeeID,
                                 @RequestParam("oldPwd") String oldPassword,
                                 @RequestParam("newPwd") String newPassword) {
        String pwdStatus = PWD_FAILED;
        if (null == employeeID || employeeID.isEmpty() || employeeID.length() > EMPLOYEE_ID_MAX_LENGTH) {
            LOGGER.error("employeeID incorrect.");
        } else if (null == oldPassword || oldPassword.isEmpty()) {
            LOGGER.error("old password empty.");
        } else if (null == newPassword || newPassword.isEmpty() || newPassword.length() < 5) {
            LOGGER.error("new password insecurity.");
        } else {
            LOGGER.info("params check valid, begin to reset password.");
            pwdStatus = userService.password(employeeID, oldPassword, newPassword);
            LOGGER.info("reset user password finished, status is: {}", pwdStatus);
        }
        return pwdStatus;
    }

    /**
     * currency number must multiple of 5
     * @param currencyNumber currency number
     * @return whether is valid
     */
    private boolean isValidCurrencyNumber(int currencyNumber) {
        return currencyNumber % 5 == 0;
    }
}
