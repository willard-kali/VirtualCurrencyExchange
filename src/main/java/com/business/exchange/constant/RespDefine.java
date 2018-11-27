package com.business.exchange.constant;

public class RespDefine {
    //normal
    public static final int CODE_SUCCESS = 0;
    public static final String DESC_SUCCESS = "success";

    public static final String DESC_LOGIN_OK = "ok";
    public static final String DESC_LOGIN_ERROR = "error";

    public static final int CODE_NEED_LOGIN = 997001;
    public static final String DESC_NEED_LOGIN = "please login first";

    //error
    //login and logout
    public static final int ERR_CODE_LOGIN_FAILED = 998001;
    public static final String ERR_DESC_LOGIN_FAILED = "login failed";

    //user
    public static final int ERR_CODE_GET_USER_INFO_FAILED = 998011;
    public static final String ERR_DESC_GET_USER_INFO_FAILED = "get user info failed";

    public static final int ERR_CODE_QUERY_HOLD_RANK_FAILED = 998012;
    public static final String ERR_DESC_QUERY_HOLD_RANK_FAILED = "query hold rank failed";

    public static final int ERR_CODE_PASSWORD_MODIFY_FAILED = 998013;
    public static final String ERR_DESC_PASSWORD_MODIFY_FAILED = "password modify failed";

    public static final int ERR_CODE_USER_CREATE_FAILED = 998014;
    public static final String ERR_DESC_USER_CREATE_FAILED = "user create failed";

    public static final int ERR_CODE_LOGOUT_FAILED = 998001;
    public static final String ERR_DESC_LOGOUT_FAILED = "login failed";

    //exchange
    public static final int ERR_CODE_EXCHANGE_FAILED = 998031;
    public static final String ERR_DESC_EXCHANGE_FAILED = "exchange failed";

    public static final int ERR_CODE_QUERY_HISTORY_BUSINESS_FAILED = 998033;
    public static final String ERR_DESC_QUERY_HISTORY_BUSINESS_FAILED = "query history exchange error";

    public static final int ERR_CODE_EXCHANGE_CURRENCY_NOT_ENOUGH_FAILED = 998034;
    public static final String ERR_DESC_EXCHANGE_CURRENCY_NOT_ENOUGH_FAILED = "exchange currency not enough";

    public static final int ERR_CODE_EXCHANGE_PARAM_EMPTY_ERROR = 998035;
    public static final String ERR_DESC_EXCHANGE_PARAM_EMPTY_ERROR = "param should be not empty";

    public static final int ERR_CODE_EXCHANGE_DEST_USER_ERROR = 998036;
    public static final String ERR_DESC_EXCHANGE_DEST_USER_ERROR = "dest user error";

    public static final int ERR_CODE_EXCHANGE_DEST_NOT_MATCH_ERROR = 998036;
    public static final String ERR_DESC_EXCHANGE_DEST_NOT_MATCH_ERROR = "dest employeeID and username not match";

    public static final int ERR_CODE_EXCHANGE_CURRENT_NOT_BE_DEST = 998037;
    public static final String ERR_DESC_EXCHANGE_CURRENT_NOT_BE_DEST = "dest employeeID must not be current";

    public static final int ERR_CODE_EXCHANGE_CURRENCY_IS_INVALID = 998038;
    public static final String ERR_DESC_EXCHANGE_CURRENCY_IS_INVALID = "exchange currency number is invalid.";

    //task
    public static final int ERR_CODE_TASK_CREATE_FAILED = 998050;
    public static final String ERR_DESC_TASK_CREATE_FAILED = "task create failed";

    public static final int ERR_CODE_TASK_FINISH_FAILED = 998051;
    public static final String ERR_DESC_TASK_FINISH_FAILED = "task finish failed";

    public static final int ERR_CODE_TASK_QUERY_FAILED = 998052;
    public static final String ERR_DESC_TASK_QUERY_FAILED = "task query failed";

}
