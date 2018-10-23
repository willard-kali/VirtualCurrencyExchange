package com.business.exchange.utils;

public class CurrencyUtils {
    /**
     * currency number must multiple of 5
     * @param currencyNumber currency number
     * @return whether is valid
     */
    public static boolean isValidCurrencyNumber(int currencyNumber) {
        return currencyNumber > 0 && currencyNumber % 5 == 0;
    }
}
