package com.business.exchange.service;

import com.business.exchange.constant.RespDefine;
import com.business.exchange.domain.*;
import com.business.exchange.model.BusinessResponse;
import com.business.exchange.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class BusinessServiceImpl implements BusinessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Override
    public Response create(String currEmployeeID, String destEmployeeID, String destUserName, int exchangeCurrencyNumber, String exchangeReason) {
        Response createResponse = new Response(RespDefine.ERR_CODE_EXCHANGE_FAILED, RespDefine.ERR_DESC_EXCHANGE_FAILED);
        User destUser = userRepository.findByEmployeeID(destEmployeeID);
        if (null == destUser.getEmployeeID() || destUser.getEmployeeID().isEmpty()) {
            LOGGER.error("dest employee is invalid.");
            return createResponse;
        }

        if (!destUser.getUserName().equals(destUserName)) {
            LOGGER.error("dest employee id not match username.");
            return createResponse;
        }

        User currUser = userRepository.findByEmployeeID(currEmployeeID);
        if (null == currUser.getEmployeeID() || currUser.getEmployeeID().isEmpty()) {
            LOGGER.error("current employee is invalid.");
            return createResponse;
        }

        if (currUser.getCurrencyNumber() < exchangeCurrencyNumber) {
            LOGGER.error("currency number not enough to exchange.");
            return createResponse;
        }

        currUser.setCurrencyNumber(currUser.getCurrencyNumber() - exchangeCurrencyNumber);
        destUser.setCurrencyNumber(destUser.getCurrencyNumber() + exchangeCurrencyNumber);
        userRepository.saveAndFlush(currUser);
        userRepository.saveAndFlush(destUser);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Business business = new Business(currUser.getUserId(), destUser.getUserId(), destUserName, destEmployeeID, timestamp ,exchangeCurrencyNumber, exchangeReason);
        businessRepository.saveAndFlush(business);
        return new Response(RespDefine.CODE_SUCCESS, RespDefine.DESC_SUCCESS);
    }

    @Override
    public String assign() {
        return null;
    }

    @Override
    public String inflowRank() {

        return null;
    }

    /**
     * 查询交易记录
     * @param userId 用户ID
     * @param employeeID 用来校验的Session中的工号
     * @return 交易记录列表
     */
    @Override
    public BusinessResponse history(int userId, String employeeID) {
        BusinessResponse historyQueryResp = new BusinessResponse(RespDefine.ERR_CODE_QUERY_HISTORY_BUSINESS_FAILED,
                RespDefine.ERR_DESC_QUERY_HISTORY_BUSINESS_FAILED);

        User user = userRepository.findByEmployeeID(employeeID);

        if (null == user || user.getUserId() != userId) {
            LOGGER.error("current user's session invalid.");
            return historyQueryResp;
        }

        List<Business> businesses = businessRepository
                .findAllBySrcUserIdEqualsOrDestUserIdEqualsOOrderByExchangeDateDesc(userId, userId);

        if (null == businesses) {
            LOGGER.error("no exchange history to record.");
            return historyQueryResp;
        }

        historyQueryResp = new BusinessResponse(RespDefine.CODE_SUCCESS, RespDefine.DESC_SUCCESS, businesses);

        return historyQueryResp;
    }
}
