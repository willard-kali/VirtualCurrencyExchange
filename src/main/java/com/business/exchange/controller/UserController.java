package com.business.exchange.controller;

import com.business.exchange.constant.RespDefine;
import com.business.exchange.domain.User;
import com.business.exchange.model.*;
import com.business.exchange.service.UserService;
import com.business.exchange.utils.CurrencyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

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

    /**
     * 登录(增加了produces反而接口出错，后续分析)
     * @param loginModel 登录信息
     * @param session HttpSession
     * @return LoginResponse
     */
    @RequestMapping(value = "login", method = RequestMethod.POST/*, produces = "application/json;charset=UTF-8"*/)
    public LoginResponse login(@RequestBody LoginModel loginModel, HttpSession session) {
        LoginResponse response = new LoginResponse(RespDefine.DESC_LOGIN_ERROR, "");
        if (null == loginModel || null == loginModel.getEmployeeID() || null == loginModel.getPassword()) {
            LOGGER.error("login invalid.");
            return response;
        }

        String employeeID = loginModel.getEmployeeID();
        String password = loginModel.getPassword();

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
        LOGGER.info("{} login in.", employeeID);
        return response;
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public BaseResponse logout(HttpSession session) {
        BaseResponse logoutResponse = new BaseResponse(RespDefine.ERR_CODE_LOGOUT_FAILED,
                RespDefine.ERR_DESC_LOGOUT_FAILED);
        if (null == session || null == session.getId()) {
            LOGGER.error("session is null, do not need to logout.");
            return logoutResponse;
        }

        String employeeID = session.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString();
        session.removeAttribute(session.getId());
        session.removeAttribute(SESSION_EMPLOYEE_ID_NAME);
        session.removeAttribute(SESSION_USER_TYPE_NAME);
        logoutResponse = new BaseResponse(RespDefine.CODE_SUCCESS, RespDefine.DESC_SUCCESS);
        LOGGER.info("{} logout.", employeeID);
        return logoutResponse;
    }

    @RequestMapping(value = "info", method = RequestMethod.GET)
    public UserProfileResponse getInfo(HttpSession session) {
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

        userInfoResp = userService.info(employeeID);
        LOGGER.info("get {} info.", employeeID);
        return userInfoResp;
    }

    private static final int DEPARTMENT_MAX_LENGTH = 1000;

    private static final int USER_GROUP_MAX_LENGTH = 500;

    /**
     * create one user
     * @param user user
     * @return create result
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public BaseResponse create(@RequestBody(required = true) User user) {
        BaseResponse createResponse = new BaseResponse(RespDefine.ERR_CODE_USER_CREATE_FAILED,
                RespDefine.ERR_DESC_USER_CREATE_FAILED);

        if (null == user
                || null == user.getUserName()
                || null == user.getEmployeeID()
                || null == user.getDepartment()
                || null == user.getUserGroup()
                || user.getCurrencyNumber() < 0) {
            LOGGER.error("user to create is invalid.");
            return createResponse;
        }

        String userName = user.getUserName();
        String employeeID = user.getEmployeeID();
        String department = user.getDepartment();
        String group = user.getUserGroup();
        int currencyNumber = user.getCurrencyNumber();

        if (userName.isEmpty() || userName.length() > USERNAME_MAX_LENGTH ) {
            LOGGER.error("userName is incorrect.");
            return createResponse;
        }
        if (employeeID.isEmpty() || employeeID.length() > EMPLOYEE_ID_MAX_LENGTH) {
            LOGGER.error("employeeID is incorrect.");
            return createResponse;
        }
        if (department.isEmpty() || department.length() > DEPARTMENT_MAX_LENGTH) {
            LOGGER.error("department is incorrect.");
            return createResponse;
        }
        if (group.isEmpty() || group.length() > USER_GROUP_MAX_LENGTH) {
            LOGGER.error("group is incorrect.");
            return createResponse;
        }
        if (!CurrencyUtils.isValidCurrencyNumber(currencyNumber)) {
            LOGGER.error("currencyNumber is incorrect.");
            return createResponse;
        }

        createResponse = userService.create(userName, employeeID, department, group, currencyNumber);

        LOGGER.info("create user {}.", employeeID);
        return createResponse;
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

    @RequestMapping(value = "/query_all", method = RequestMethod.GET)
    public String getUserInfo() {
        UserQueryResult users = userService.queryAll();
        return users.toString();
    }

    /*@RequestMapping(value = "/modify", method = RequestMethod.GET)
    public String modifyUserInfo(@RequestParam("user") User user) {
        //todo
        return "";
    }*/

    @RequestMapping(value = "/password", method = RequestMethod.POST)
    public BaseResponse modifyPassword(@RequestBody(required = true) PwdModifiedModel pwdModifiedModel,
                                   HttpSession session) {
        BaseResponse pwdResponse = new BaseResponse(RespDefine.ERR_CODE_PASSWORD_MODIFY_FAILED,
                RespDefine.ERR_DESC_PASSWORD_MODIFY_FAILED);
        //校验session
        if (null == session
                || null == session.getAttribute(SESSION_EMPLOYEE_ID_NAME)
                || session.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString().isEmpty()) {
            LOGGER.error("current session invalid.");
            return pwdResponse;
        }
        //校验参数
        if (null == pwdModifiedModel
                || null == pwdModifiedModel.getOldPassword()
                || null == pwdModifiedModel.getNewPassword()) {
            LOGGER.error("password modify params error.");
            return pwdResponse;
        }

        String employeeID = session.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString();
        String oldPassword = pwdModifiedModel.getOldPassword();
        String newPassword = pwdModifiedModel.getNewPassword();

        if (employeeID.isEmpty() || employeeID.length() > EMPLOYEE_ID_MAX_LENGTH) {
            LOGGER.error("employeeID is incorrect.");
            return pwdResponse;
        }
        if (oldPassword.isEmpty() || oldPassword.length() > PASSWORD_MAX_LENGTH) {
            LOGGER.error("old password is incorrect.");
            return pwdResponse;
        }
        if (newPassword.isEmpty() || newPassword.length() > PASSWORD_MAX_LENGTH) {
            LOGGER.error("new password is incorrect.");
            return pwdResponse;
        }

        User user = userService.queryUser(employeeID);
        if (null == user || user.getUserId() <= 0) {
            LOGGER.error("query user by employeeID {} error.", employeeID);
            return pwdResponse;
        }

        //修改操作的工号和session工号不一致时，禁止操作
        if (!session.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString().equals(employeeID)) {
            LOGGER.error("current user session invalid.");
            return pwdResponse;
        }

        pwdResponse = userService.password(user.getUserId(), oldPassword, newPassword);
        LOGGER.info("set new password for {}.", employeeID);
        return pwdResponse;
    }

    @RequestMapping(value = "hold_rank", method = RequestMethod.GET)
    public UserResponse currencyHoldRank() {
        LOGGER.info("query hold rank.");
        return userService.holdRank();
    }

    private static final String ADMIN_EMPLOYEE_ID = "admin";

    @RequestMapping(value = "import_accounts", method = RequestMethod.POST, consumes = "multipart/form-data")
    public boolean importAccounts(MultipartFile accountsFile, HttpSession session) {
        LOGGER.info("import accounts.");

        //校验session
        if (null == session
                || null == session.getAttribute(SESSION_EMPLOYEE_ID_NAME)
                || session.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString().isEmpty()) {
            LOGGER.error("current session invalid.");
            return false;
        }

        String employeeID = session.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString();

        if (!ADMIN_EMPLOYEE_ID.equals(employeeID)) {
            LOGGER.error("must admin import accounts.");
            return false;
        }

        return userService.importAccounts(accountsFile);
    }
}
