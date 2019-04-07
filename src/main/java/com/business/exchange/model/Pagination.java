package com.business.exchange.model;

public class Pagination {
    private int total;
    private int pageSize;
    private int current;

    public Pagination(int total, int pageSize, int current) {
        this.total = total;
        this.pageSize = pageSize;
        this.current = current;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }
}
