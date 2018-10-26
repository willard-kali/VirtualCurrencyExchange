package com.business.exchange.controller;

import com.business.exchange.constant.RespDefine;
import com.business.exchange.domain.Business;
import com.business.exchange.model.BusinessResponse;
import com.business.exchange.model.Response;
import com.business.exchange.service.BusinessService;
import com.business.exchange.utils.CurrencyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

import static com.business.exchange.constant.BusinessConstants.*;
import static com.business.exchange.constant.UserConstants.EMPLOYEE_ID_MAX_LENGTH;
import static com.business.exchange.constant.UserConstants.USERNAME_MAX_LENGTH;

/**
 * 交易接口
 */
@RestController
@RequestMapping("/exchange")
public class BusinessController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessController.class);

    @Autowired
    private BusinessService businessService;

    /**
     * 交易
     * @param currEmployeeID
     * @param destEmployeeID
     * @param destUserName
     * @param exchangeCurrencyNumber
     * @param exchangeReason
     * @return
     */
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public Response deal(@RequestParam(value = "currEmployeeID", required = true) String currEmployeeID,
                       @RequestParam(value = "destEmployeeID", required = true) String destEmployeeID,
                       @RequestParam(value = "destUserName", required = true) String destUserName,
                       @RequestParam(value = "exchangeCurrencyNumber", required = true) int exchangeCurrencyNumber,
                       @RequestParam(value = "exchangeReason", required = true) String exchangeReason) {
        Response createResponse = new Response(RespDefine.ERR_CODE_EXCHANGE_FAILED, RespDefine.ERR_DESC_EXCHANGE_FAILED);
        if (null == currEmployeeID || currEmployeeID.isEmpty() || currEmployeeID.length() > EMPLOYEE_ID_MAX_LENGTH) {
            LOGGER.error("current user incorrect.");
        } else if (null == destEmployeeID || destEmployeeID.isEmpty() || destEmployeeID.length() > EMPLOYEE_ID_MAX_LENGTH) {
            LOGGER.error("dest employee incorrect.");
        } else if (null == destUserName || destUserName.isEmpty() || destUserName.length() > USERNAME_MAX_LENGTH) {
            LOGGER.error("dest username incorrect.");
        } else if (!CurrencyUtils.isValidCurrencyNumber(exchangeCurrencyNumber)) {
            LOGGER.error("exchange currency number invalid.");
        } else if (null == exchangeReason || exchangeReason.isEmpty() || exchangeReason.length() > EXCHANGE_REASON_MAX_LENGTH) {
            LOGGER.error("exchange reason incorrect.");
        } else {
            createResponse = businessService.create(currEmployeeID, destEmployeeID, destUserName, exchangeCurrencyNumber, exchangeReason);
        }
        return createResponse;
    }

    @RequestMapping(value = "assign", method = RequestMethod.GET)
    public String assign(int currUserId, List<String> destEmployeeIDs, int exchangeCurrencyNumber, String exchangeReason) {
        //todo
        return "";
    }

    @RequestMapping(value = "inflow_rank", method = RequestMethod.GET)
    public String currencyInflowRank(String order, int size) {
        //todo
        return "";
    }

    private static final String SESSION_EMPLOYEE_ID_NAME = "employeeID";

    @RequestMapping(value = "history", method = RequestMethod.GET)
    public BusinessResponse exchangeHistory(@RequestParam(value = "userId", required = true) int userId,
                                            HttpSession httpSession) {
        BusinessResponse historyQueryResponse = new BusinessResponse(RespDefine.ERR_CODE_QUERY_HISTORY_BUSINESS_FAILED,
                RespDefine.ERR_DESC_QUERY_HISTORY_BUSINESS_FAILED);
        if (userId <= 0) {
            LOGGER.error("userId invalid.");
            return historyQueryResponse;
        }

        if (null == httpSession || null == httpSession.getAttribute(SESSION_EMPLOYEE_ID_NAME)
                || httpSession.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString().isEmpty()) {
            LOGGER.error("current user session invalid.");
            return historyQueryResponse;
        }

        historyQueryResponse = businessService.history(userId, httpSession.getAttribute(SESSION_EMPLOYEE_ID_NAME).toString());

        return historyQueryResponse;
    }
}
