package com.business.exchange.domain;

public class Business {
    private int id;
    private int srcUserId;
    private int destUserId;
    private String exchangeDate;
    private int exchangeCurrencyNumber;
    private String exchangeReason;

    /**
     * id自动生成，其他字段必填
     * @param srcUserId 交易来源用户ID
     * @param destUserId 交易目的用户ID
     * @param exchangeDate 交易日期
     * @param exchangeCurrencyNumber 交易货币数量
     * @param exchangeReason 交易理由
     */
    public Business(int srcUserId, int destUserId, String exchangeDate, int exchangeCurrencyNumber, String exchangeReason) {
        this.srcUserId = srcUserId;
        this.destUserId = destUserId;
        this.exchangeDate = exchangeDate;
        this.exchangeCurrencyNumber = exchangeCurrencyNumber;
        this.exchangeReason = exchangeReason;
    }

    /**
     * exchangeCurrencyNumber必须是5的整数倍
     * @return 是否有效的交易额
     */
    public boolean isExchangeCurrencyNumberValid() {
        return this.exchangeCurrencyNumber % 5 == 0;
    }

}
