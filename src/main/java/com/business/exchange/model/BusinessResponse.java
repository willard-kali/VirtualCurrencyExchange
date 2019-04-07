package com.business.exchange.model;

import com.business.exchange.domain.Business;

import java.util.List;

public class BusinessResponse extends BaseResponse {

    private List<Business> businesses;

    private Pagination pagination;

    public BusinessResponse() {
        super();
    }

    public BusinessResponse(int resultCode, String errDesc) {
        super(resultCode, errDesc);
    }

    public BusinessResponse(int resultCode, String errDesc, List<Business> businesses, Pagination pagination) {
        super(resultCode, errDesc);
        this.businesses = businesses;
        this.pagination = pagination;
    }

    public List<Business> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
