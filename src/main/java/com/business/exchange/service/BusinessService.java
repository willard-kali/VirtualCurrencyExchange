package com.business.exchange.service;

import org.springframework.stereotype.Service;

public interface BusinessService {

    String create(String currEmployeeID, String destEmployeeID, String destUserName, int exchangeCurrencyNumber, String exchangeReason);

    String assign();

    String ownRank();

    String inflowRank();

    String history();

}
