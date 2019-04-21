package com.business.exchange.controller;

import com.business.exchange.constant.RespDefine;
import com.business.exchange.domain.Business;
import com.business.exchange.model.*;
import com.business.exchange.service.BusinessService;
import com.business.exchange.utils.CurrencyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static com.business.exchange.constant.UserConstants.EMPLOYEE_ID_MAX_LENGTH;
import static com.business.exchange.constant.UserConstants.USERNAME_MAX_LENGTH;

/**
 * 交易接口
 */
@RestController
@RequestMapping("/exchange")
public class BusinessController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessController.class);

    private static final int EXCHANGE_REASON_MAX_LENGTH = 1000;

    private static final int ASSIGN_MAX_MONEY = 1000;

    @Autowired
    private BusinessService businessService;

    /**
     * 交易
     * @param business 交易信息
     * @return 交易是否成功
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public BaseResponse create(@RequestBody(required = true) Business business, HttpSession session) {
        BaseResponse createResponse = new BaseResponse(RespDefine.ERR_CODE_EXCHANGE_FAILED, RespDefine.ERR_DESC_EXCHANGE_FAILED);

        if (null == business
                || null == business.getDestEmployeeID()
                || null == business.getDestUserName()
                || null == business.getExchangeReason()
                || null == session) {
            LOGGER.error("business is null.");
            createResponse = new BaseResponse(RespDefine.ERR_CODE_EXCHANGE_PARAM_EMPTY_ERROR,
                    RespDefine.ERR_DESC_EXCHANGE_PARAM_EMPTY_ERROR);
            return createResponse;
        }

        if (null == session.getAttribute(SESSION_EMPLOYEE_ID_NAME)
                || session.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString().isEmpty()) {
            LOGGER.error("session is invalid.");
            return createResponse;
        }

        String sessionEmployeeID = session.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString();

//        int currUserID = business.getSrcUserId();
        String destEmployeeID = business.getDestEmployeeID();
        String destUserName = business.getDestUserName();
        int exchangeCurrencyNumber = business.getExchangeCurrencyNumber();
        String exchangeReason = business.getExchangeReason();

        /*if (currUserID <= 0) {
            LOGGER.error("current user incorrect.");
            return createResponse;
        }*/
        if (null == destEmployeeID || destEmployeeID.isEmpty() || destEmployeeID.length() > EMPLOYEE_ID_MAX_LENGTH) {
            LOGGER.error("dest employee incorrect.");
            return createResponse;
        }
        if (null == destUserName || destUserName.isEmpty() || destUserName.length() > USERNAME_MAX_LENGTH) {
            LOGGER.error("dest username incorrect.");
            return createResponse;
        }
        if (!CurrencyUtils.isValidCurrencyNumber(exchangeCurrencyNumber)) {
            LOGGER.error("exchange currency number invalid.");
            return createResponse;
        }
        if (exchangeReason.isEmpty() || exchangeReason.length() > EXCHANGE_REASON_MAX_LENGTH) {
            LOGGER.error("exchange reason incorrect.");
            return createResponse;
        }

        createResponse = businessService.create(sessionEmployeeID, destEmployeeID, destUserName,
                exchangeCurrencyNumber, exchangeReason);

        LOGGER.info("create exchange to {}.", destEmployeeID);
        return createResponse;
    }

    //统一新增okr币
    @RequestMapping(value = "assign", method = RequestMethod.GET)
    public BaseResponse assign(List<String> employeeIDs, int exchangeCurrencyNumber, String assignDesc) {

        BaseResponse assignResp = new BaseResponse();

        if (null == employeeIDs || null == assignDesc || exchangeCurrencyNumber <= 0) {
            LOGGER.error("input param error.");
            assignResp = new BaseResponse(RespDefine.ERR_CODE_EXCHANGE_PARAM_EMPTY_ERROR,
                    RespDefine.ERR_DESC_EXCHANGE_PARAM_EMPTY_ERROR);
            return assignResp;
        }

        return businessService.assign(employeeIDs, exchangeCurrencyNumber, assignDesc);
    }

    private static final String ADMIN_EMPLOYEE_ID = "admin";

    //统一新增okr币
    @RequestMapping(value = "assignall", method = RequestMethod.POST)
    public BaseResponse assignAll(@RequestBody AssignRequest assignRequest, HttpSession session) {

        if (null == session || null == session.getAttribute(SESSION_EMPLOYEE_ID_NAME)
                || session.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString().isEmpty()
                || !ADMIN_EMPLOYEE_ID.equals(session.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString())) {
            LOGGER.error("current user session invalid.");
            return new BaseResponse(RespDefine.ERR_CODE_USER_INVALID_ASSIGN_FAILED,
                    RespDefine.ERR_DESC_USER_INVALID_ASSIGN_FAILED);
        }

        if (null == assignRequest
                || null == assignRequest.getAssignDesc()
                || assignRequest.getExchangeCurrencyNumber() <= 0
                || assignRequest.getExchangeCurrencyNumber() >= ASSIGN_MAX_MONEY) {
            LOGGER.error("input param error.");
            return new BaseResponse(RespDefine.ERR_CODE_EXCHANGE_PARAM_EMPTY_ERROR,
                    RespDefine.ERR_DESC_EXCHANGE_PARAM_EMPTY_ERROR);
        }

        LOGGER.info("assign all by admin.");
        return businessService.assignAll(assignRequest.getExchangeCurrencyNumber(), assignRequest.getAssignDesc());
    }

    //todo
    @RequestMapping(value = "inflow_rank", method = RequestMethod.GET)
    public String currencyInflowRank(String order, int size) {
        //todo
        return "";
    }

    private static final String SESSION_EMPLOYEE_ID_NAME = "employeeID";

    @RequestMapping(value = "history", method = RequestMethod.GET)
    public BusinessResponse exchangeHistory(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                            HttpSession session) {
        BusinessResponse historyQueryResponse = new BusinessResponse(RespDefine.ERR_CODE_QUERY_HISTORY_BUSINESS_FAILED,
                RespDefine.ERR_DESC_QUERY_HISTORY_BUSINESS_FAILED);

        if (null == session || null == session.getAttribute(SESSION_EMPLOYEE_ID_NAME)
                || session.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString().isEmpty()) {
            LOGGER.error("current user session invalid.");
            return historyQueryResponse;
        }

        String employeeID = session.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString();

        historyQueryResponse = businessService.history(currentPage, pageSize, employeeID);
        LOGGER.info("query {} exchange history.", employeeID);
        return historyQueryResponse;
    }

    /**
     * just for view
     * @param session
     * @return
     */
    @RequestMapping(value = "menu", method = RequestMethod.GET)
    public Menu menu(HttpSession session) {

        List<MenuButton> buttons = new ArrayList<MenuButton>();
        buttons.add(
                new MenuButton("xxx1", "发起交易", "https://gw.alipayobjects.com/zos/rmsportal/zOsKZmFRdUtvpqCImOVY.png", "转账OKR币给他人", "../form/step-form/info", "")
        );
        buttons.add(
                new MenuButton("xxx2", "发布任务", "https://gw.alipayobjects.com/zos/rmsportal/WdGqmHpayyMjiEhcKoVE.png", "发布一条OKR币悬赏任务", "../list/table-list", "")
        );
        buttons.add(
                new MenuButton("xxx3", "查看账单", "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1540308190&di=0739c3f4bbfe477b89ffd1083d2e5291&src=http://pic.weifengke.com/attachments/2/2524/c9dedb70c59694afa4df6948b3c73c2c.jpg", "查看OKR币收入支出历史", "../form/bill-form", "")
        );
        Menu menu = new Menu(buttons);

        return menu;
    }
}
