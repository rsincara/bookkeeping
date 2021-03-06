package com.boots.model;

import com.boots.entity.Balance;
import com.boots.entity.TransactionType;

import java.util.ArrayList;

public class BalanceWithTransactions {
    public String balanceName;
    public Long balanceId;
    public Double amount;
    public ArrayList<TransactionType> transactions;

    public BalanceWithTransactions (Balance balance, Double amount, ArrayList<TransactionType> transactions) {
        this.balanceName = balance.getName();
        this.balanceId = balance.getId();
        this.amount = amount;
        this.transactions = transactions;
    }

    public String getBalanceName() {
        return balanceName;
    }

    public void setBalanceName(String balanceName) {
        this.balanceName = balanceName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public ArrayList<TransactionType> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<TransactionType> transactions) {
        this.transactions = transactions;
    }
}
