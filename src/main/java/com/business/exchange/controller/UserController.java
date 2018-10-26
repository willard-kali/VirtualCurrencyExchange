package com.business.exchange.controller;

import com.business.exchange.constant.RespDefine;
import com.business.exchange.domain.*;
import com.business.exchange.model.*;
import com.business.exchange.service.BusinessService;
import com.business.exchange.service.UserService;
import com.business.exchange.utils.CurrencyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskDecorator;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static com.business.exchange.constant.UserConstants.*;

/**
 * User Management Request
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private static final String SESSION_EMPLOYEE_ID_NAME = "employeeID";

    private static final String SESSION_USER_TYPE_NAME = "type";

    @Autowired
    private UserService userService;

    @Autowired
    private BusinessService businessService;

    /**
     * 登录(增加了produces反而接口出错，后续分析)
     * @param user User
     * @param session HttpSession
     * @return LoginResponse
     */
    @RequestMapping(value = "login", method = RequestMethod.POST/*, produces = "application/json;charset=UTF-8"*/)
    public LoginResponse login(@RequestBody User user, HttpSession session) {
        LOGGER.info("login into...");
        LoginResponse response = new LoginResponse(RespDefine.DESC_LOGIN_ERROR, "");
        if (null == user || null == user.getEmployeeID() || null == user.getPassword()) {
            LOGGER.error("login invalid.");
            return response;
        }

        String employeeID = user.getEmployeeID();
        String password = user.getPassword();

        if (employeeID.isEmpty()
                || employeeID.length() > EMPLOYEE_ID_MAX_LENGTH
                || password.isEmpty()
                || password.length() > PASSWORD_MAX_LENGTH) {
            LOGGER.error("login invalid.");
            return response;
        }

        response = userService.login(employeeID, password);
        //if succeed, set session
        if (response.getStatus().equals(RespDefine.DESC_LOGIN_OK)) {
            session.setAttribute(session.getId(), employeeID);
            session.setAttribute(SESSION_EMPLOYEE_ID_NAME, employeeID);
            session.setAttribute(SESSION_USER_TYPE_NAME, response.getCurrentAuthority());
        }
        LOGGER.info("login out...");
        return response;
    }

    @RequestMapping(value = "info", method = RequestMethod.GET)
    public UserProfileResponse getInfo(HttpSession session) {
        LOGGER.info("info into...");
        UserProfileResponse userInfoResp = new UserProfileResponse(RespDefine.ERR_CODE_GET_USER_INFO_FAILED,
                RespDefine.ERR_DESC_GET_USER_INFO_FAILED);
        if (null == session) {
            LOGGER.error("session invalid.");
            return userInfoResp;
        }
        String employeeID = session.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString();

        if (null == employeeID || employeeID.isEmpty() || employeeID.length() > EMPLOYEE_ID_MAX_LENGTH) {
            LOGGER.error("employeeID invalid.");
            return userInfoResp;
        }

        User user = userService.queryUser(employeeID);

        if (null != user.getPassword()) {
            user.setPassword("");
        }

        if (null == user.getEmployeeID() || user.getEmployeeID().isEmpty()) {
            LOGGER.error("user invalid.");
            return userInfoResp;
        }

        BusinessResponse businessResponse = businessService.history(user.getUserId());

        if (null == businessResponse || null == businessResponse.getBusinesses()) {
            LOGGER.error("business response error.");
            return userInfoResp;
        }

        UserResponse holdRankusers = userService.holdRank();
        int total = holdRankusers.getUsers().size();
        int rank = 1;
        for (User holdRankUser : holdRankusers.getUsers()) {
            if (holdRankUser.getEmployeeID() != null && user.getEmployeeID().equals(holdRankUser.getEmployeeID())) {
                break;
            } else {
                rank++;
            }
        }

        userInfoResp = new UserProfileResponse(RespDefine.CODE_SUCCESS, RespDefine.DESC_SUCCESS, user, rank, total, businessResponse.getBusinesses().size());


        LOGGER.info("info out...");
        LOGGER.info("info response: {}.", userInfoResp.toString());
        return userInfoResp;
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
        } else if (currencyNumber < 0 || !CurrencyUtils.isValidCurrencyNumber(currencyNumber)) {
            LOGGER.error("currencyNumber incorrect.");
        } else {
            LOGGER.info("params check valid, begin to create.");
            createStatus = userService.create(userName, employeeID, department, group, currencyNumber);
            LOGGER.info("create user finished, status is: {}", createStatus);
        }
        return createStatus;
    }


    /*@RequestMapping(value = "/query", method = RequestMethod.GET)
    public String getUserInfo(@RequestParam("employeeID") String employeeID) {
        if (null == employeeID || "".equals(employeeID) || employeeID.length() > EMPLOYEE_ID_MAX_LENGTH) {
            LOGGER.error("employeeID not correct.");
            return new UserQueryResult(0, new ArrayList<>()).toString();
        }
        UserQueryResult users = userService.query(employeeID);
        return users.toString();
    }*/

    /*@RequestMapping(value = "/query_all", method = RequestMethod.GET)
    public String getUserInfo(HttpServletRequest request) {
        UserQueryResult users = userService.queryAll();
        return users.toString();
    }*/

    /*@RequestMapping(value = "/modify", method = RequestMethod.GET)
    public String modifyUserInfo(@RequestParam("user") User user) {
        //todo
        return "";
    }*/

    @RequestMapping(value = "/password", method = RequestMethod.GET)
    public Response modifyPassword(@RequestParam("employeeID") String employeeID,
                                   @RequestParam("oldPwd") String oldPassword,
                                   @RequestParam("newPwd") String newPassword,
                                   HttpSession httpSession) {
        Response pwdResponse = new Response(RespDefine.ERR_CODE_PASSWORD_MODIFY_FAILED,
                RespDefine.ERR_DESC_PASSWORD_MODIFY_FAILED);
        //校验session
        if (null == httpSession
                || null == httpSession.getAttribute(SESSION_EMPLOYEE_ID_NAME)
                || httpSession.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString().isEmpty()) {
            LOGGER.error("current session invalid.");
            return pwdResponse;
        }
        if (null == employeeID || employeeID.isEmpty() || employeeID.length() > EMPLOYEE_ID_MAX_LENGTH) {
            LOGGER.error("employeeID incorrect.");
            return pwdResponse;
        }
        if (null == oldPassword || oldPassword.isEmpty() || oldPassword.length() > PASSWORD_MAX_LENGTH) {
            LOGGER.error("old password incorrect.");
            return pwdResponse;
        }
        if (null == newPassword || newPassword.isEmpty() || newPassword.length() > PASSWORD_MAX_LENGTH) {
            LOGGER.error("new password incorrect.");
            return pwdResponse;
        }
        //修改操作的工号和session工号不一致时，禁止操作
        if (!httpSession.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString().equals(employeeID)) {
            LOGGER.error("current user session invalid.");
            return pwdResponse;
        }

        pwdResponse = userService.password(employeeID, oldPassword, newPassword);

        return pwdResponse;
    }

    @RequestMapping(value = "hold_rank", method = RequestMethod.GET)
    public UserResponse currencyHoldRank() {
        return userService.holdRank();
    }
}
