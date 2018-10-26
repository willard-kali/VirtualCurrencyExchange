package com.business.exchange.model;

public class Response {
    private int resultCode;

    private String errDesc;

    public Response() {
    }

    public Response(int resultCode, String errDesc) {
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

    /*@Override
    public String toString() {
        return "Response{" +
                "resultCode=" + resultCode +
                ", errDesc='" + errDesc + '\'' +
                '}';
    }*/
}
