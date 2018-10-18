package com.business.exchange.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 交易接口
 */
@RestController
@RequestMapping("/exchange")
public class BusinessController {

    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String create(int currUserId, String destEmployeeID, int exchangeCurrencyNumber, String exchangeReason) {
        //todo
        return "";
    }

    @RequestMapping(value = "assign", method = RequestMethod.GET)
    public String assign(int currUserId, List<String> destEmployeeIDs, int exchangeCurrencyNumber, String exchangeReason) {
        //todo
        return "";
    }

    @RequestMapping(value = "own_rank", method = RequestMethod.GET)
    public String currencyOwnRank(String order, int size) {
        //todo
        return "";
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
