package com.business.exchange.service;

import com.business.exchange.constant.RespDefine;
import com.business.exchange.domain.Business;
import com.business.exchange.domain.BusinessRepository;
import com.business.exchange.domain.User;
import com.business.exchange.domain.UserRepository;
import com.business.exchange.model.BaseResponse;
import com.business.exchange.model.BusinessResponse;
import com.business.exchange.model.Pagination;
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
    public BaseResponse create(String sessionEmployeeID, String destEmployeeID, String destUserName, int exchangeCurrencyNumber, String exchangeReason) {
        BaseResponse createResponse = new BaseResponse(RespDefine.ERR_CODE_EXCHANGE_FAILED, RespDefine.ERR_DESC_EXCHANGE_FAILED);
        User destUser = userRepository.findByEmployeeID(destEmployeeID);
        if (null == destUser) {
            LOGGER.error("dest user is invalid.");
            createResponse = new BaseResponse(RespDefine.ERR_CODE_EXCHANGE_DEST_USER_ERROR,
                    RespDefine.ERR_DESC_EXCHANGE_DEST_USER_ERROR);
            return createResponse;
        }

        if (!destUser.getUserName().equals(destUserName)) {
            LOGGER.error("dest employee id not match username.");
            createResponse = new BaseResponse(RespDefine.ERR_CODE_EXCHANGE_DEST_NOT_MATCH_ERROR,
                    RespDefine.ERR_DESC_EXCHANGE_DEST_NOT_MATCH_ERROR);
            return createResponse;
        }

        User currUser = userRepository.findByEmployeeID(sessionEmployeeID);
        if (null == currUser || null == currUser.getEmployeeID() || currUser.getEmployeeID().isEmpty()) {
            LOGGER.error("current employee is invalid.");
            return createResponse;
        }

        if (currUser.getEmployeeID().equals(destEmployeeID)) {
            LOGGER.error("src user cannot be equals to dest user.");
            createResponse = new BaseResponse(RespDefine.ERR_CODE_EXCHANGE_CURRENT_NOT_BE_DEST,
                    RespDefine.ERR_DESC_EXCHANGE_CURRENT_NOT_BE_DEST);
            return createResponse;
        }

        if (currUser.getCurrencyNumber() < exchangeCurrencyNumber) {
            LOGGER.error("currency number not enough to exchange.");
            createResponse = new BaseResponse(RespDefine.ERR_CODE_EXCHANGE_CURRENCY_NOT_ENOUGH_FAILED,
                    RespDefine.ERR_DESC_EXCHANGE_CURRENCY_NOT_ENOUGH_FAILED);
            return createResponse;
        }

        currUser.setCurrencyNumber(currUser.getCurrencyNumber() - exchangeCurrencyNumber);
        destUser.setCurrencyNumber(destUser.getCurrencyNumber() + exchangeCurrencyNumber);
        userRepository.saveAndFlush(currUser);
        userRepository.saveAndFlush(destUser);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Business business = new Business(currUser.getUserId(), currUser.getUserName(), destUser.getUserId(), destUserName, destEmployeeID, timestamp ,exchangeCurrencyNumber, exchangeReason);
        businessRepository.saveAndFlush(business);
        createResponse = new BaseResponse(RespDefine.CODE_SUCCESS, RespDefine.DESC_SUCCESS);
        return createResponse;
    }

    @Override
    public String inflowRank() {

        return null;
    }

    /**
     * 查询交易记录
     * @param employeeID 用来校验的Session中的工号
     * @return 交易记录列表
     */
    @Override
    public BusinessResponse history(int currentPage, int pageSize, String employeeID) {
        BusinessResponse historyQueryResp = new BusinessResponse(RespDefine.ERR_CODE_QUERY_HISTORY_BUSINESS_FAILED,
                RespDefine.ERR_DESC_QUERY_HISTORY_BUSINESS_FAILED);

        User user = userRepository.findByEmployeeID(employeeID);

        if (null == user) {
            LOGGER.error("current user's session invalid.");
            return historyQueryResp;
        }

        int userId = user.getUserId();
        List<Business> businesses = businessRepository
                .findAllBySrcUserIdEqualsOrDestUserIdEqualsOrderByExchangeDateDesc(userId, userId);

        Pagination pagination = new Pagination(businesses.size(), pageSize, currentPage);

        int pageBegin = (currentPage - 1) * pageSize;
        int pageEnd = currentPage * pageSize;

        if (pageEnd > businesses.size()) {
            pageEnd = businesses.size();
        }
        List<Business> pageBusinesses = businesses.subList(pageBegin, pageEnd);

        if (null == pageBusinesses) {
            LOGGER.error("no exchange history to record.");
            return historyQueryResp;
        }

        historyQueryResp = new BusinessResponse(RespDefine.CODE_SUCCESS, RespDefine.DESC_SUCCESS, pageBusinesses, pagination);

        return historyQueryResp;
    }

    @Override
    public boolean assign(List<String> employeeIDs, int okrAssignNumber, String assignDesc) {

        for (String employeeID : employeeIDs) {
            if (employeeID.isEmpty() || !userRepository.existsByEmployeeID(employeeID)) {
                LOGGER.error("employeeID: {} not exist.", employeeID);
                return false;
            }
        }
        User adminUser = userRepository.findByEmployeeID("admin");

        for (String employeeID: employeeIDs) {
            User user = userRepository.findByEmployeeID(employeeID);
            user.setCurrencyNumber(user.getCurrencyNumber() + okrAssignNumber);
            userRepository.saveAndFlush(user);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Business business = new Business(
                    adminUser.getUserId(),
                    adminUser.getUserName(),
                    user.getUserId(),
                    user.getUserName(),
                    user.getEmployeeID(),
                    timestamp,
                    okrAssignNumber,
                    assignDesc);
            businessRepository.saveAndFlush(business);
        }
        return true;
    }
}
