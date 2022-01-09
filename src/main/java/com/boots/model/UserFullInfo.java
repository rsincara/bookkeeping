package com.boots.model;

import java.util.ArrayList;

public class UserFullInfo {
    public String userName;
    public ArrayList<BalanceWithTransactions> balancesWithTransactions;
    public Double generalBalance;
    public ArrayList<BalanceOnDate> balanceOnDates;

    public UserFullInfo() {
        this.generalBalance = 0d;
    }

    public ArrayList<BalanceOnDate> getBalanceOnDates() {
        return balanceOnDates;
    }

    public void setBalanceOnDates(ArrayList<BalanceOnDate> balanceOnDates) {
        this.balanceOnDates = balanceOnDates;
    }

    public Double getGeneralBalance() {
        return generalBalance;
    }

    public void setGeneralBalance(Double generalBalance) {
        this.generalBalance = generalBalance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<BalanceWithTransactions> getBalancesWithTransactions() {
        return balancesWithTransactions;
    }

    public void setBalancesWithTransactions(ArrayList<BalanceWithTransactions> balancesWithTransactions) {
        this.balancesWithTransactions = balancesWithTransactions;
    }
}
