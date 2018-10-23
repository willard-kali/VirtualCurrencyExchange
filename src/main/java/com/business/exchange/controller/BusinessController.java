package com.business.exchange.controller;

import com.business.exchange.service.BusinessService;
import com.business.exchange.utils.CurrencyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String create(String currEmployeeID, String destEmployeeID, String destUserName, int exchangeCurrencyNumber, String exchangeReason) {
        String createStatus = CREATE_SUCCESS;
        if (null == currEmployeeID || currEmployeeID.isEmpty() || currEmployeeID.length() > EMPLOYEE_ID_MAX_LENGTH) {
            LOGGER.error("current user incorrect.");
            createStatus = CREATE_FAILED;
        } else if (null == destEmployeeID || destEmployeeID.isEmpty() || destEmployeeID.length() > EMPLOYEE_ID_MAX_LENGTH) {
            LOGGER.error("dest employee incorrect.");
            createStatus = DEST_USER_FAILED;
        } else if (null == destUserName || destUserName.isEmpty() || destUserName.length() > USERNAME_MAX_LENGTH) {
            LOGGER.error("dest username incorrect.");
            createStatus = DEST_USER_FAILED;
        } else if (!CurrencyUtils.isValidCurrencyNumber(exchangeCurrencyNumber)) {
            LOGGER.error("not enough money.");
            createStatus = BALANCE_NOT_ENOUGH;
        } else if (null == exchangeReason || exchangeReason.isEmpty() || exchangeReason.length() > EXCHANGE_REASON_MAX_LENGTH) {
            LOGGER.error("exchange reason incorrect.");
            createStatus = EXCHANGE_REASON_EMPTY;
        } else {
            createStatus = businessService.create(currEmployeeID, destEmployeeID, destUserName, exchangeCurrencyNumber, exchangeReason);
        }
        return createStatus;
    }

    @RequestMapping(value = "assign", method = RequestMethod.GET)
    public String assign(int currUserId, List<String> destEmployeeIDs, int exchangeCurrencyNumber, String exchangeReason) {
        //todo
        return "";
    }

    @RequestMapping(value = "own_rank", method = RequestMethod.GET)
    public String currencyOwnRank() {
        return businessService.ownRank();
    }

    @RequestMapping(value = "inflow_rank", method = RequestMethod.GET)
    public String currencyInflowRank(String order, int size) {
        //todo
        return "";
    }

    @RequestMapping(value = "history", method = RequestMethod.GET)
    public String exchangeHistory() {
        //todo
        return "";
    }
}
