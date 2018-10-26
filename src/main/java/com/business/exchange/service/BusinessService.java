package com.business.exchange.service;

import com.business.exchange.model.BusinessResponse;
import com.business.exchange.model.Response;
import org.springframework.stereotype.Service;

public interface BusinessService {

    Response create(String currEmployeeID, String destEmployeeID, String destUserName, int exchangeCurrencyNumber, String exchangeReason);

    String assign();

    String inflowRank();

    BusinessResponse history(int userId, String employeeID);

}
