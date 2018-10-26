package com.business.exchange.model;

import com.business.exchange.domain.Business;
import com.business.exchange.model.Response;

import java.util.List;

public class BusinessResponse extends Response {

    private List<Business> businesses;

    public BusinessResponse() {
        super();
    }

    public BusinessResponse(int resultCode, String errDesc) {
        super(resultCode, errDesc);
    }

    public BusinessResponse(int resultCode, String errDesc, List<Business> businesses) {
        super(resultCode, errDesc);
        this.businesses = businesses;
    }

    public List<Business> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }
}
