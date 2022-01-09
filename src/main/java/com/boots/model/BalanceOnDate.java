package com.boots.model;

import java.sql.Date;

public class BalanceOnDate {
    private Date date;
    private Double amount;

    public BalanceOnDate() {
        this.amount = 0d;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
