package com.business.exchange.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int exchangeId;

    private int srcUserId;
    private String srcUserName;
    private int destUserId;
    private String destUserName;
    private String destEmployeeID;
    private Timestamp exchangeDate;
    private int exchangeCurrencyNumber;
    private String exchangeReason;

    public Business() {
    }

    /**
     * id自动生成，其他字段必填
     * @param srcUserId 交易来源用户ID
     * @param destUserId 交易目的用户ID
     * @param exchangeDate 交易日期
     * @param exchangeCurrencyNumber 交易货币数量
     * @param exchangeReason 交易理由
     */
    public Business(int srcUserId, int destUserId, Timestamp exchangeDate, int exchangeCurrencyNumber, String exchangeReason) {
        this.srcUserId = srcUserId;
        this.destUserId = destUserId;
        this.exchangeDate = exchangeDate;
        this.exchangeCurrencyNumber = exchangeCurrencyNumber;
        this.exchangeReason = exchangeReason;
    }

    /**
     *
     * @param srcUserId
     * @param srcUserName
     * @param destUserId
     * @param destUserName
     * @param destEmployeeID
     * @param exchangeDate
     * @param exchangeCurrencyNumber
     * @param exchangeReason
     */
    public Business(int srcUserId, String srcUserName, int destUserId, String destUserName, String destEmployeeID, Timestamp exchangeDate, int exchangeCurrencyNumber, String exchangeReason) {
        this.srcUserId = srcUserId;
        this.srcUserName = srcUserName;
        this.destUserId = destUserId;
        this.destUserName = destUserName;
        this.destEmployeeID = destEmployeeID;
        this.exchangeDate = exchangeDate;
        this.exchangeCurrencyNumber = exchangeCurrencyNumber;
        this.exchangeReason = exchangeReason;
    }

    public int getSrcUserId() {
        return srcUserId;
    }

    public String getSrcUserName() {
        return srcUserName;
    }

    public int getDestUserId() {
        return destUserId;
    }

    public String getDestUserName() {
        return destUserName;
    }

    public String getDestEmployeeID() {
        return destEmployeeID;
    }

    public Timestamp getExchangeDate() {
        return exchangeDate;
    }

    public int getExchangeCurrencyNumber() {
        return exchangeCurrencyNumber;
    }

    public String getExchangeReason() {
        return exchangeReason;
    }

    public int getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(int exchangeId) {
        this.exchangeId = exchangeId;
    }

    public void setSrcUserId(int srcUserId) {
        this.srcUserId = srcUserId;
    }

    public void setDestUserId(int destUserId) {
        this.destUserId = destUserId;
    }

    public void setDestUserName(String destUserName) {
        this.destUserName = destUserName;
    }

    public void setDestEmployeeID(String destEmployeeID) {
        this.destEmployeeID = destEmployeeID;
    }

    public void setExchangeDate(Timestamp exchangeDate) {
        this.exchangeDate = exchangeDate;
    }

    public void setExchangeCurrencyNumber(int exchangeCurrencyNumber) {
        this.exchangeCurrencyNumber = exchangeCurrencyNumber;
    }

    public void setExchangeReason(String exchangeReason) {
        this.exchangeReason = exchangeReason;
    }

    @Override
    public String toString() {
        return "Business{" +
                "exchangeId=" + exchangeId +
                ", srcUserId=" + srcUserId +
                ", destUserId=" + destUserId +
                ", destUserName='" + destUserName + '\'' +
                ", destEmployeeID='" + destEmployeeID + '\'' +
                ", exchangeDate=" + exchangeDate +
                ", exchangeCurrencyNumber=" + exchangeCurrencyNumber +
                ", exchangeReason='" + exchangeReason + '\'' +
                '}';
    }
}
