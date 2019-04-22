package com.business.exchange.service;

import com.business.exchange.constant.RespDefine;
import com.business.exchange.domain.Business;
import com.business.exchange.domain.BusinessRepository;
import com.business.exchange.domain.User;
import com.business.exchange.domain.UserRepository;
import com.business.exchange.model.BaseResponse;
import com.business.exchange.model.BusinessResponse;
import com.business.exchange.model.Pagination;
import com.business.exchange.model.UserType;
import com.business.exchange.utils.ExcelAnalyzer;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class BusinessServiceImpl implements BusinessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessServiceImpl.class);

    //账单名称
    private static final String BILL_NAME = "账单";


    @Value(value = "#{${excel.title.mapping}}")
    private Map<String, String> excelTitleMapping;

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
    public BusinessResponse historyAll(String employeeID) {
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

        historyQueryResp = new BusinessResponse(RespDefine.CODE_SUCCESS, RespDefine.DESC_SUCCESS, businesses);

        return historyQueryResp;
    }

    @Override
    public BaseResponse assign(List<String> employeeIDs, int okrAssignNumber, String assignDesc) {
        BaseResponse assignResp = new BaseResponse(RespDefine.CODE_SUCCESS, RespDefine.DESC_SUCCESS);
        for (String employeeID : employeeIDs) {
            if (employeeID.isEmpty() || !userRepository.existsByEmployeeID(employeeID)) {
                LOGGER.error("employeeID: {} not exist.", employeeID);
                assignResp = new BaseResponse(RespDefine.ERR_CODE_BUSINESS_ASSIGN_EMPLOYEE_NOT_EXIST_FAILED,
                        RespDefine.ERR_DESC_BUSINESS_ASSIGN_EMPLOYEE_NOT_EXIST_FAILED);
                return assignResp;
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
        return assignResp;
    }

    @Override
    public BaseResponse assignAll(String employeeID, int exchangeCurrencyNumber, String assignDesc) {
        List<User> allUsers = userRepository.findAll();
        User adminUser = userRepository.findByEmployeeID(employeeID);

        List<User> toUpdateUsers = new ArrayList<>();
        List<Business> toUpdateBusinesses = new ArrayList<>();
        for (User user : allUsers) {
            if (employeeID.equals(user.getEmployeeID())) {
                continue;
            }
            user.setCurrencyNumber(user.getCurrencyNumber() + exchangeCurrencyNumber);
            toUpdateUsers.add(user);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Business business = new Business(
                    adminUser.getUserId(),
                    adminUser.getUserName(),
                    user.getUserId(),
                    user.getUserName(),
                    user.getEmployeeID(),
                    timestamp,
                    exchangeCurrencyNumber,
                    assignDesc);
            toUpdateBusinesses.add(business);
        }
        userRepository.saveAll(toUpdateUsers);
        businessRepository.saveAll(toUpdateBusinesses);
        return new BaseResponse(RespDefine.CODE_SUCCESS, RespDefine.DESC_SUCCESS);
    }

    @Override
    public File exportExchangeBill(String employeeID) {
        List<Business> businesses = null;
        User user = userRepository.findByEmployeeID(employeeID);
        //如果是管理员用户，下载全部账单
        if (UserType.ADMIN_USER.equals(user.getUserType())) {
            businesses = businessRepository.findAllByOrderByExchangeDateDesc();
        } else {
            //如果是普通用户，下载个人账单
            int userId = user.getUserId();
            businesses = businessRepository.findAllBySrcUserIdEqualsOrDestUserIdEqualsOrderByExchangeDateDesc(userId, userId);
        }
        //获取要导出的账单表头字段
        Set<String> titles = excelTitleMapping.keySet();
        String[] titleArray = new String[excelTitleMapping.size()];
        int index = 0;
        //获取表头字段的中文翻译
        for (String titleKey : titles) {
            titleArray[index] = excelTitleMapping.get(titleKey);
            index++;
        }
        //导出时间
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = now.format(format);
        HSSFWorkbook excel = null;
        File file = new File(BILL_NAME + "-" + employeeID + "-" + today + ".xlsx");
        try {
            //生成要导出的Excel，包含数据
            excel = ExcelAnalyzer.getHSSFWorkbook(
                    BILL_NAME,
                    titleArray,
                    buildBill(businesses)
            );
            excel.write(file);
            LOGGER.info("create excel succeed.");
            return file;
        } catch (Exception e) {
            LOGGER.error("{} exception: {}.",e.getClass().getName(), e);
            return file;
        }
    }

    /**
     * 生成账单
     * @param businesses 交易记录
     * @return
     */
    private String[][] buildBill(List<Business> businesses) throws NoSuchFieldException, IllegalAccessException {
        int columnNumber = excelTitleMapping.size();
        int rowNumber = businesses.size();
        String[][] bills = new String[rowNumber][columnNumber];
        int i = 0;
        for (Business business : businesses) {
            int j = 0;
            for (String titleName : excelTitleMapping.keySet()) {
                Class<?> clazz = business.getClass();
                Field field = clazz.getDeclaredField(titleName);
                field.setAccessible(true);
                Object value = field.get(business);
                bills[i][j] = value.toString();
                j++;
            }
            i++;
        }
        return bills;
    }

}
