package com.business.exchange.controller;

import com.business.exchange.constant.RespDefine;
import com.business.exchange.domain.Business;
import com.business.exchange.model.BaseResponse;
import com.business.exchange.model.BusinessResponse;
import com.business.exchange.service.BusinessService;
import com.business.exchange.utils.CurrencyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
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

    //todo
    @RequestMapping(value = "assign", method = RequestMethod.GET)
    public String assign(int currUserId, List<String> destEmployeeIDs, int exchangeCurrencyNumber, String exchangeReason) {
        //todo
        return "";
    }

    //todo
    @RequestMapping(value = "inflow_rank", method = RequestMethod.GET)
    public String currencyInflowRank(String order, int size) {
        //todo
        return "";
    }

    private static final String SESSION_EMPLOYEE_ID_NAME = "employeeID";

    @RequestMapping(value = "history", method = RequestMethod.GET)
    public BusinessResponse exchangeHistory(HttpSession session) {
        BusinessResponse historyQueryResponse = new BusinessResponse(RespDefine.ERR_CODE_QUERY_HISTORY_BUSINESS_FAILED,
                RespDefine.ERR_DESC_QUERY_HISTORY_BUSINESS_FAILED);

        if (null == session || null == session.getAttribute(SESSION_EMPLOYEE_ID_NAME)
                || session.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString().isEmpty()) {
            LOGGER.error("current user session invalid.");
            return historyQueryResponse;
        }

        String employeeID = session.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString();

        historyQueryResponse = businessService.history(employeeID);
        LOGGER.info("query {} exchange history.", employeeID);
        return historyQueryResponse;
    }
}
