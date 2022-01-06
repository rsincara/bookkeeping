package com.boots.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_transactions")
public class TransactionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private ETransactionTypes transactionType;
    private Double amount;
    private String commentary;
    private Date date;

    public TransactionType() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTransactionType(ETransactionTypes transactionType) {
        this.transactionType = transactionType;
    }

    public ETransactionTypes getTransactionType() {
        return transactionType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
