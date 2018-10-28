package com.business.exchange.model;

public class BaseResponse {
    private int resultCode;

    private String errDesc;

    public BaseResponse() {
    }

    public BaseResponse(int resultCode, String errDesc) {
        this.resultCode = resultCode;
        this.errDesc = errDesc;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrDesc() {
        return errDesc;
    }

    public void setErrDesc(String errDesc) {
        this.errDesc = errDesc;
    }

}
