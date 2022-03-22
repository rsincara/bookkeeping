package com.boots.model;

public class NewBalanceForm {
    public String userName;
    public String balanceName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBalanceName() {
        return balanceName;
    }

    public void setBalanceName(String balanceName) {
        this.balanceName = balanceName;
    }

    public Double getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(Double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public Double balanceAmount;

}
