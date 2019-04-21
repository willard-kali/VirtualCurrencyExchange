package com.business.exchange.service;

import com.business.exchange.model.BaseResponse;
import com.business.exchange.model.BusinessResponse;

import java.util.List;

public interface BusinessService {

    BaseResponse create(String sessionEmployeeID, String destEmployeeID, String destUserName, int exchangeCurrencyNumber, String exchangeReason);

    String inflowRank();

    BusinessResponse history(int currentPage, int pageSize, String employeeID);

    BaseResponse assign(List<String> employeeIDs, int exchangeCurrencyNumber, String assignDesc);

    BaseResponse assignAll(int exchangeCurrencyNumber, String assignDesc);
}
