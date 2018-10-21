package com.business.exchange.domain;

import java.io.Serializable;

public class BaseResponse implements Serializable {

    private boolean ok;

    private String message;

    private Object data;

    public BaseResponse() {
    }

    public BaseResponse(boolean ok) {
        this.ok = ok;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
