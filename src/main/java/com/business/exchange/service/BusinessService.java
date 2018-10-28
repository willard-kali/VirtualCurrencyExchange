package com.business.exchange.service;

import com.business.exchange.model.BaseResponse;
import com.business.exchange.model.BusinessResponse;

public interface BusinessService {

    BaseResponse create(String sessionEmployeeID, String destEmployeeID, String destUserName, int exchangeCurrencyNumber, String exchangeReason);

    String assign();

    String inflowRank();

    BusinessResponse history(String employeeID);

}
