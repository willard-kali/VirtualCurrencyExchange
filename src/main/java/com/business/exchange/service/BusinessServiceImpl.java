package com.business.exchange.service;

import com.business.exchange.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.business.exchange.constant.BusinessConstants.*;

@Service
public class BusinessServiceImpl implements BusinessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Override
    public String create(String currEmployeeID, String destEmployeeID, String destUserName, int exchangeCurrencyNumber, String exchangeReason) {

        User destUser = userRepository.findByEmployeeID(destEmployeeID);
        if (!destUser.isValid()) {
            LOGGER.error("dest employee is invalid.");
            return CREATE_FAILED;
        }

        if (!destUser.getUserName().equals(destUserName)) {
            LOGGER.error("dest employee id not match username.");
            return DEST_USER_FAILED;
        }

        User currUser = userRepository.findByEmployeeID(currEmployeeID);
        if (!currUser.isValid()) {
            LOGGER.error("current employee is invalid.");
            return CREATE_FAILED;
        }

        if (currUser.getCurrencyNumber() < exchangeCurrencyNumber) {
            LOGGER.error("currency number not enough to exchange.");
            return BALANCE_NOT_ENOUGH;
        }

        currUser.setCurrencyNumber(currUser.getCurrencyNumber() - exchangeCurrencyNumber);
        destUser.setCurrencyNumber(destUser.getCurrencyNumber() + exchangeCurrencyNumber);
        userRepository.saveAndFlush(currUser);
        userRepository.saveAndFlush(destUser);

        Business business = new Business(currUser.getUserId(), destUser.getUserId(), "2018" ,exchangeCurrencyNumber, exchangeReason);
        businessRepository.saveAndFlush(business);
        return CREATE_SUCCESS;
    }

    @Override
    public String assign() {
        return null;
    }

    @Override
    public String ownRank() {
        List<User> ownRankUsers = userRepository.findAll(Sort.by(Sort.Order.desc("currencyNumber")));
        StringBuffer sb = new StringBuffer("{ \"size\": " + ownRankUsers.size() + ", \"users\":[");
        for (User user : ownRankUsers) {
            sb.append(user.toRankString()).append(",");
        }
        sb = sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append("]}");
        return sb.toString();
    }

    @Override
    public String inflowRank() {
        return null;
    }

    @Override
    public String history() {
        return null;
    }
}
