package com.business.exchange.domain;

import java.io.Serializable;

public class BusinessRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private String srcUserName;

    private String srcEmployeeID;

    private String destUserName;

    private String destEmployeeID;

    private int exchangeCurrencyNumber;

    private String exchangeDate;

    public BusinessRecord() {
    }

    public BusinessRecord(String srcUserName, String srcEmployeeID,
                          String destUserName, String destEmployeeID,
                          int exchangeCurrencyNumber, String exchangeDate) {
        this.srcUserName = srcUserName;
        this.srcEmployeeID = srcEmployeeID;
        this.destUserName = destUserName;
        this.destEmployeeID = destEmployeeID;
        this.exchangeCurrencyNumber = exchangeCurrencyNumber;
        this.exchangeDate = exchangeDate;
    }

    @Override
    public String toString() {
        return "BusinessRecord{" +
                "srcUserName='" + srcUserName + '\'' +
                ", srcEmployeeID='" + srcEmployeeID + '\'' +
                ", destUserName='" + destUserName + '\'' +
                ", destEmployeeID='" + destEmployeeID + '\'' +
                ", exchangeCurrencyNumber=" + exchangeCurrencyNumber +
                ", exchangeDate='" + exchangeDate + '\'' +
                '}';
    }
}
